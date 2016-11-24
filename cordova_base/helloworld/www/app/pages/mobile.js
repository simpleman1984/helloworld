//必须注意下面的顺序
define("config", ["/app/ext/libs/requirejs/config.js"]);
//定义总入口
define("app", ["angular", "angularAMD", "angular-ui-router"], function (angular, angularAMD) {

    // routes
    var registerRoutes = function ($stateProvider, $urlRouterProvider) {

        // default
        $urlRouterProvider.otherwise("/index");

        // route
        $stateProvider.state("index", angularAMD.route({
            url: "/index",
            templateUrl: "/app/pages/mobile/index.html",
            controllerUrl: "/app/pages/mobile/index.js"
        }));

        $stateProvider.state("alarm", angularAMD.route({
            url: "/alarm",
            templateUrl: "/app/pages/mobile/alarm.html",
            controllerUrl: "/app/pages/mobile/alarm.js"
        }));

        $stateProvider.state("analyse", angularAMD.route({
            url: "/analyse",
            templateUrl: "/app/pages/mobile/analyse.html",
            controllerUrl: "/app/pages/mobile/analyse.js"
        }));

        $stateProvider.state("focus", angularAMD.route({
            url: "/focus",
            templateUrl: "/app/pages/mobile/focus.html",
            controllerUrl: "/app/pages/mobile/focus.js"
        }));

        $stateProvider.state("mine", angularAMD.route({
            url: "/mine",
            templateUrl: "/app/pages/mobile/mine.html",
            controllerUrl: "/app/pages/mobile/mine.js"
        }));

        $stateProvider.state("login", angularAMD.route({
            url: "/login",
            templateUrl: "/app/pages/mobile/login.html",
            controllerUrl: "/app/pages/mobile/login.js"
        }));

    };
    // module
    var app = angular.module("app", ["ui.router"], function ($controllerProvider) {
        window.controllerProvider = $controllerProvider;
    });
    //底部菜单控制器
    app.controller('navCtrl', function ($scope) {
        $scope.menus =
            {
                items: [
                    {
                        name: "能耗",
                        url: "#index"
                    },
                    {
                        name: "预警",
                        url: "#alarm"
                    },
                    {
                        name: "分析",
                        url: "#analyse"
                    },
                    {
                        name: "关注",
                        url: "#focus"
                    },
                    {
                        name: "我的",
                        url: "#mine"
                    }
                ]
            }
        ;
    });
    //对外暴露当前页面
    window.app = app;
      // config
    app.config(["$stateProvider", "$urlRouterProvider", registerRoutes]);
    // bootstrap
    return angularAMD.bootstrap(app);
});
//加载config，调用默认的app模块
require(["config"]);