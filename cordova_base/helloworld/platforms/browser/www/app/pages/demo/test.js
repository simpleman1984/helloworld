define(["asyncLoader","directivejstree","directivedateranger","echarts"], function (asyncLoader,directivejstree,directivedateranger,echarts) {
	
	// controller
	return ["$scope", function ($scope) {
		$scope.title = "这是test.html的标题引用";
		$scope.showModal = function(){
			asyncLoader.showModal({
				view:"/app/pages/demo/show.html",
				viewModel:"/app/pages/demo/show.js"

			});
		}

		// 初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'));
		// 指定图表的配置项和数据
		var option = {
			title: {
				text: '示例'
			},
			tooltip: {},
			legend: {
				data:['销量1']
			},
			xAxis: {
				data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
			},
			yAxis: {},
			series: [{
				name: '销量1',
				type: 'bar',
				data: [5, 20, 36, 10, 10, 20]
			}]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}];
});