// bootstrap
define(["angular", "angularAMD","bootstrap", "angular-ui-router"], function (angular, angularAMD) {
        
    // routes
    var registerRoutes = function($stateProvider, $urlRouterProvider) {
        	
        // default
        $urlRouterProvider.otherwise("/home");
        
        // route
        $stateProvider
        
            // home
            .state("home", angularAMD.route({
                url: "/home",
                templateUrl: "../modules/base/home/home.html",
                controllerUrl: "../modules/base/home/home.js"
            }))
        
            // ngloadtest
            .state("ngloadtest", angularAMD.route({
                url: "/ngloadtest",
                templateUrl: "../modules/base/ngloadtest.html",
                controllerUrl: "../modules/base/ngloadtest.js"
            }))
			
			// home
            .state("about", angularAMD.route({
                url: "/about/{aboutid}",
                templateUrl: "../modules/base/about.html",
                controllerUrl: "../modules/base/about.js"
            }))
        
            //service
            .state("service", angularAMD.route({
                url: "/service",
                templateUrl: "../modules/base/service.html",
                controllerUrl: "../modules/base/service.js"
            }))
        
            //controller
            .state("controller", angularAMD.route({
                url: "/controller",
                templateUrl: "../modules/base/controller.html",
                controllerUrl: "../modules/base/controller.js"
            }))
        
            //filter
            .state("filter", angularAMD.route({
                url: "/filter",
                templateUrl: "../modules/base/filter.html",
                controllerUrl: "../modules/base/filter.js"
            }))
        
            //directive
            .state("directive", angularAMD.route({
                url: "/directive",
                templateUrl: "../modules/base/directive.html",
                controllerUrl: "../modules/base/directive.js"
            }))
        ;   		
    };        
        
    // module
    var app = angular.module("app", ["ui.router"],function($controllerProvider){
        window.controllerProvider = $controllerProvider;
    });
    
    //对外暴露当前页面
    window.app = app;

    // config
    app.config(["$stateProvider", "$urlRouterProvider", registerRoutes]);
    
    //定义provider
    app.provider('value', function UnicornLauncherProvider() {
        console.info("value：我只执行一次");
        
        var val ;
        var RET = function(_c){
            var c = _c ;
            this.v = function(){
                return c;
            }
        };
        this.value = function(value) {
            val = "provider:_" + value;
        };
        
        this.$get = function () {
            return new RET(val);
        };
    
    });
    app.config(["valueProvider", function(valueProvider) {
        console.info("valueProvider：我只执行一次");
        
        valueProvider.value("inited");
    }]);
    
    //生成 UUID方法
    var UUID = (function (uuidRegEx, uuidReplacer) { 
        return function () { 
            return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(uuidRegEx, uuidReplacer).toUpperCase(); 
        };
     })(/[xy]/g, function (c) {
         var r = Math.random() * 16 | 0, 
         v = c =="x"? r : (r & 3 | 8); 
         return v.toString(16); 
    });
    
    function registerController(moduleName, controllerName, template, container) {
        //container = container || 'body';
        // Load html file with content that uses Ctrl controller
        // var view = $(template).appendTo(container)
            // .attr("id",controllerName)
            // .attr("ng-controller",controllerName);
        // Here I cannot get the controller function directly so I
        // need to loop through the module's _invokeQueue to get it
        var queue = angular.module(moduleName)._invokeQueue;
        for(var i=0;i<queue.length;i++) {
            var call = queue[i];
            if(call[0] == "$controllerProvider" &&
                call[1] == "register" &&
                call[2][0] == controllerName) {
                    controllerProvider.register(controllerName, call[2][1]);
                };
        };

        angular.injector(['ng', 'app']).invoke(function($compile, $rootScope) {
            $compile($('#'+controllerName))($rootScope);
            $rootScope.$apply();
        });
    };
    
    //自定义插件（加载requirejs对应的js和html）
    app.directive('ngTest', function($compile){
        return {
            restrict : 'A',
            link : function (scope, elem , attrs, ctrl) {
                //读取视图和模型
                var controllerUrl = attrs.controllerUrl;
                var templateUrl = attrs.controllerHtml;
                
                var uuid = UUID();
                //异步加载对应的html和js
                require([controllerUrl,"text!"+templateUrl], function (ctrl,view){
                        $(view).appendTo("body")
                            .attr("id",uuid)
                            .attr("ng-controller",uuid);

                        //定义controller
                        app.controller(uuid,ctrl); 

                        //注册controller，插入element
                        //moduleName controllerName templateHtml
                        registerController("app",uuid,view);
                });
            }
        }
    });
    
    app.controller("bottomCtl",["$scope",function($scope){
        $scope.title="底部title"; 
    }]);

    // bootstrap
    return angularAMD.bootstrap(app);
});