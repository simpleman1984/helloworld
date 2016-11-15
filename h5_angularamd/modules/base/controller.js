define(["app"], function (app) {
    //定义service基础示例
    app.factory('notify', ['$window', function(win) {
        return "notify_msg"; 
    }]);
    
    var MyController = function($scope, notify) {
        $scope.title = "notify传递过来的参数:" + notify;
    };
	MyController.$inject = ['$scope', 'notify'];
    
    return MyController;
});