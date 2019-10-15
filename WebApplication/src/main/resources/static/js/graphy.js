var myChart = echarts.init($('#graphx-chart')[0]);
myChart.showLoading();
$.get('/gexf/graph.gexf', function (xml) {
    myChart.hideLoading();
    var today = new Date();
    var graph = echarts.dataTool.gexf.parse(xml);
    var categories = [{name:'城市'},{name:'学历'},{name:'职业'}];
    graph.nodes.forEach(function (node) {
        node.itemStyle = null;
        node.symbolSize = 16;
        node.category = node.attributes.NodeType;
        // Use random x, y
        node.x = node.y = null;
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
                        position: 'right',
                        fontSize: 200
                    }
                },
                force: {
                    repulsion: 10000,
                    edgeLength: [400,2000]
                }
            }
        ]
    };

    myChart.setOption(option);
    myChart.on('click', function (param){
        console.log('param---->', param);  // 打印出param, 可以看到里边有很多参数可以使用
        //获取节点点击的数组序号
        var arrayIndex = param.dataIndex;
        console.log('arrayIndex---->', arrayIndex);
        console.log('name---->', param.name);
        if (param.dataType == 'node') {
            console.log("点击了节点" + param.name);
            getRequest("graphx/computeNode/"+param.dataIndex,
                function (res) {
                    console.log(res.content);
                },
                 function (error) {
                    alert(error);
                }
            );
        }
    });

}, 'xml');
