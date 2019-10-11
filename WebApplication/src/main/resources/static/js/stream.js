var streamChart = echarts.init($('#stream-chart')[0]);

var currentIndex = -1;//记录插入表的index
var lastIndex = -1;//记录数据库中最大的index
var today = new Date();
var charts;var i = 0;
var option = {
    title : {
        text: '职业招聘需求',
        subtext: '截止至'+today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate()
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['数量']
    },
    toolbox: {
        show : true,
        feature : {
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            data : ['未知','未知','未知','未知','未知','未知','未知','未知','未知']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'数量',
            type:'bar',
            data:[0, 0, 0, 0, 0, 0, 0, 0, 0],
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name: '平均值'}
                ]
            }
        }
    ],
    animationDelay: function(k){
        if (k < (1 / 2.75)) { return 7.5625 * k * k; }
        else if (k < (2 / 2.75)) { return 7.5625 * (k -= (1.5 / 2.75)) * k + 0.75; }
        else if (k < (2.5 / 2.75)) { return 7.5625 * (k -= (2.25 / 2.75)) * k + 0.9375; }
        else { return 7.5625 * (k -= (2.625 / 2.75)) * k + 0.984375;}
    },
    color: ['#6640ff']
};

streamChart.setOption(option);

var indexInterval = setInterval(function(){
    getRequestF(
        'stream/getStartIndex',
        function (res) {
            currentIndex = res.content;
        },
         function (error) {
            alert(error);
        }
    );
    if(currentIndex!=-1){
        clearInterval(indexInterval);
        var requestInterval = setInterval(function(){
            getRequestF(
                'stream/getLastIndex',
                function (res) {
                    lastIndex = res.content;
                },
                 function (error) {
                    alert(error);
                }
            );
            getRequestF(
                'stream/get/'+currentIndex,
                function (res) {
                    console.log(res);
                    option.series[0].data = res.content.yData;
                    option.xAxis[0].data = res.content.xData;
                    streamChart.setOption(option);
                },
                 function (error) {
                    alert(error);
                }
            );
            if(currentIndex<lastIndex)currentIndex++;
        },500);
    }
},3000);


//一次性获取数据库中的所有数据
//getRequestF(
//    'stream/get',
//    function (res) {
//        console.log(res);
//        charts = res.content;
//    },
//     function (error) {
//        alert(error);
//    }
//);
//
//
//setInterval(function(){
//    if(i<charts.length){
//        option.series[0].data = charts[i].yData;
//        option.xAxis[0].data = charts[i].xData;
//        streamChart.setOption(option);
//        i++;
//    }
//},500);