define(["app"], function (app) {
    //定义service基础示例
    app.constant('planetName', 'constant:Greasy Giant');
    app.value('clientId', 'value:a12345654321x');
    
    app.factory('notify', ['$window', function(win) {
        console.info("factory：我只执行一次");
        return "factory:notify"; 
     }]);
    
    app.service('notifyService', ['$window', function(win) {
        console.info("Service：我只执行一次");
        return "notify_msg"; 
    }]);
    
	// controller
	return ["$scope","notify","notifyService","clientId","planetName","value", function ($scope,notify,notifyService,clientId,planetName,value) {
        console.info(value);
		$scope.title = notify + "___" + clientId + "____" + planetName+"____"+value.v();
	}];	
    
});