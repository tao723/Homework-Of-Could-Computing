var myChart = echarts.init($('#graphx-chart')[0]);
myChart.showLoading();
var pt1 = -1;
var pt2 = -1;
var types = ['城市','学历','职业'];
$.get('/gexf/graph.gexf', function (xml) {
    myChart.hideLoading();
    var today = new Date();
    var graph = echarts.dataTool.gexf.parse(xml);
    var categories = [{name:'城市'},{name:'学历'},{name:'职业'}];
    graph.nodes.forEach(function (node) {
        node.itemStyle = null;
        node.symbolSize = 9;
        node.category = node.attributes.NodeType;
        // Use random x, y
        node.x = null;
        node.y = null;
        node.draggable = false;
    });
    option = {
        title: {
            text: '招聘信息关系图',
            subtext: '截止至'+today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate(),
            top: 'top',
            left: 'middle'
        },
        tooltip: {show:false},
        legend: [{
            // selectedMode: 'single',
            data: categories.map(function (a) {
                return a.name;
            }),
            top: 'bottom'
        }],
        animation: false,
        series : [
            {
                type: 'graph',
                layout: 'force',
                data: graph.nodes,
                links: graph.links,
                categories: categories,
                roam: true,
                label: {
                    normal: {
                        position: 'top',
                        fontSize: 75
                    }
                },
                force: {
                    repulsion: 10000,
//                    layoutAnimation: false,
                    gravity: 0.01
                }
            }
        ],
        backgroundColor: '#ffffff',
        color: ['#c23531','#20b2aa','#ffcc00']
    };

    myChart.setOption(option);
    myChart.on('click', function (param){
        console.log('param---->', param);  // 打印出param, 可以看到里边有很多参数可以使用
        //获取节点点击的数组序号
        if (param.dataType == 'node') {
            console.log("点击了节点" + param.name);
            getRequest("graphx/computeNode/"+param.data.id,
                function (res) {
                    pt1 = res.content.id1;
                    pt2 = res.content.id2;
                    var alertMsg = "与"+types[param.data.category]+param.name+"联系最紧密的为:"+res.content.name1+","+res.content.name2;

                    for (var i =0;i<option.series[0].data.length;i++){
                        if(option.series[0].data[i].id==pt1||option.series[0].data[i].id==pt2||option.series[0].data[i].id==param.data.id){
                            option.series[0].data[i].symbolSize = 30;
                        }
                        else option.series[0].data[i].symbolSize = 9;
                    }
                    myChart.setOption(option);
                    sleep(2000);
                    alert(alertMsg);
                },
                 function (error) {
                    alert(error);
                }
            );
        }
    });

}, 'xml');
