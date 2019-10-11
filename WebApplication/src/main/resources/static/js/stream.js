var streamChart = echarts.init($('#stream-chart')[0]);

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
            data : ['A','B','C','D','E','F','G','H','I','J','K','L']
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
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
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

getRequestF(
    'stream/get',
    function (res) {
        console.log(res);
        charts = res.content;
    },
     function (error) {
        alert(error);
    }
);


setInterval(function(){
    if(i<charts.length){
        option.series[0].data = charts[i].yData;
        option.xAxis[0].data = charts[i].xData;
        streamChart.setOption(option);
        i++;
    }
},1500);