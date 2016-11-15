define(["app"], function (app) {
    //定义service基础示例
    app.factory('notify', ['$window', function(win) {
        return "notify_msg"; 
     }]);
    
	// controller
	return ["$scope","notify", function ($scope,notify) {
		$scope.title = notify;
	}];	
    
});