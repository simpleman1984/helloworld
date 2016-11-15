define(["app"], function (app) {
    //定义service基础示例
    app.constant('planetName', 'Greasy Giant');
    app.value('clientId', 'a12345654321x');
    
    app.factory('notify', ['$window', function(win) {
        console.info("factory：我只执行一次");
        return "notify_msg"; 
     }]);
    
    app.service('notifyService', ['$window', function(win) {
        console.info("Service：我只执行一次");
        return "notify_msg"; 
    }]);
    
    //定义provider
    app.provider('value', function UnicornLauncherProvider() {
        console.info("value：我只执行一次");
        
        var val;
        this.value = function(value) {
            val = value + "___value";
        };
        
        this.$get = [function () {
            return val;
        }];
    
    });
    
    app.config(["valueProvider", function(valueProvider) {
        console.info("valueProvider：我只执行一次");
        
        valueProvider.value();
    }]);
    
    
	// controller
	return ["$scope","notify","notifyService","clientId","planetName", function ($scope,notify,notifyService,clientId,planetName) {
		$scope.title = notify + "___" + clientId + "____" + planetName;
	}];	
    
});