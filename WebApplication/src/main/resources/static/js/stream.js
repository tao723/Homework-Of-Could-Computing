var streamChart = echarts.init($('#stream-chart')[0]);

var today = new Date();
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
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
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
var socket;
if(typeof(WebSocket) == "undefined") {
    console.log("您的浏览器不支持WebSocket");
}
else{
    console.log("支持支持大力支持");
    //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
    socket = new WebSocket("ws://localhost:8080/stream/get");
    //打开事件
    socket.onopen = function() {
        console.log("Socket 已打开");
        //socket.send("这是来自客户端的消息" + location.href + new Date());
    };
    //获得消息事件
    socket.onmessage = function(msg) {
        console.log(msg);
        //发现消息进入    开始处理前端触发逻辑
    };
    //关闭事件
    socket.onclose = function() {
        console.log("Socket已关闭");
    };
    //发生了错误事件
    socket.onerror = function() {
        alert("Socket发生了错误");
        //此时可以尝试刷新页面
    }
}
streamChart.setOption(option);
// 使用刚指定的配置项和数据显示图表。

