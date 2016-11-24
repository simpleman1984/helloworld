define(["echarts","asyncLoader"], function (echarts,asyncLoader) {

    // controller
    return ["$scope", function ($scope) {
        // properties
        $scope.title = "这是标题引用";
        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/mobile/notices.html",
                viewModel:"/app/pages/mobile/notices.js",
                width:"90%",
                height:"30%",
                title:"消息详情"
            });
        };

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('chart1'));

        // 指定图表的配置项和数据
        var option = {

            legend: {
                orient : 'vertical',
                x : 'left',
                data:['水','电','煤','天然气']
            },

            calculable : true,
            series : [
                {
                    name:'访问来源',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:[
                        {value:335, name:'水'},
                        {value:310, name:'电'},
                        {value:234, name:'煤'},
                        {value:135, name:'天然气'}
                    ]
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }];
});
