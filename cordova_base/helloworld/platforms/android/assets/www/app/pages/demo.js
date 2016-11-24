//必须注意下面的顺序
define("config", ["/app/ext/libs/requirejs/config.js"]);
//定义总入口（自己实现，有BUG，先屏蔽掉）
//define("app", ['jkAmd','angular-ui-router'], function (jkAmd) {
//    var app = angular.module("app", ['ui.router']);
//
//    app.config(['$controllerProvider','$rootScopeProvider',
//        function (controllerProvider,rootScopeProvider) {
//            app.$controllerProvider = controllerProvider;
//            app.$rootScopeProvider = rootScopeProvider;
//        }
//    ]);
//
//    // III. inline dependency expression
//    app.config(['$stateProvider', '$urlRouterProvider',
//        function ($stateProvider, $urlRouterProvider) {
//
//            $urlRouterProvider.otherwise("/home");
//
//            $stateProvider.state("home",jkAmd.route("","/app/pages/demo/home.html","/app/pages/demo/home.js"));
//            $stateProvider.state("home.child",jkAmd.route("/home/child","/app/pages/demo/home.child.html","/app/pages/demo/home.child.js"));
//            $stateProvider.state("about",jkAmd.route("/about","/app/pages/demo/about.html","/app/pages/demo/about.js"));
//        }
//    ]);
//
//    angular.bootstrap(document, ['app'])
//
//    return app;
//});

//定义总入口
define("app", ["angular", "angularAMD", "angular-ui-router"], function (angular, angularAMD) {
    var registerRoutes = function ($stateProvider, $urlRouterProvider) {

        // default
        $urlRouterProvider.otherwise("/home");

        //首页
        $stateProvider.state("home", angularAMD.route({
            url: "/home",
            templateUrl: "/app/pages/demo/home.html",
            controllerUrl: "/app/pages/demo/home.js"
        }));

        //test
        $stateProvider.state("test", angularAMD.route({
            url: "/test",
            templateUrl:"/app/pages/demo/test.html",
            controllerUrl:"/app/pages/demo/test.js"
        }));

      //二级路由页面
        $stateProvider.state("home.child", angularAMD.route({
            url: "/child",
            templateUrl: "/app/pages/demo/home/home.child.html",
            controllerUrl: "/app/pages/demo/home/home.child.js"
        }));
        //地图页面
        $stateProvider.state("home.map", angularAMD.route({
            url: "/map",
            templateUrl: "/app/pages/demo/home/map.html",
            controllerUrl: "/app/pages/demo/home/map.js"
        }));

        //关于页面
        $stateProvider.state("home.news", angularAMD.route({
            url: "/news",
            templateUrl: "/app/pages/ditu/news.html",
            controllerUrl: "/app/pages/display/ditu/news.js"
        }));

        //图表页面
        $stateProvider.state("home.echarts_column", angularAMD.route({
            url: "/echarts/column",
            templateUrl: "/app/pages/demo/home/echarts/column.html",
            controllerUrl: "/app/pages/demo/home/echarts/column.js"
        }));

        $stateProvider.state("home.echarts_pie", angularAMD.route({
            url: "/echarts/pie",
            templateUrl: "/app/pages/demo/home/echarts/pie.html",
            controllerUrl: "/app/pages/demo/home/echarts/pie.js"
        }));


        //关于页面
        $stateProvider.state("about", angularAMD.route({
            url: "/about",
            templateUrl: "/app/pages/demo/about.html",
            controllerUrl: "/app/pages/demo/about.js"
        }));



    };

    // module
    var app = angular.module("app", ["ui.router"], function ($controllerProvider) {
        window.controllerProvider = $controllerProvider;
    });

    // config
    app.config(["$stateProvider", "$urlRouterProvider", registerRoutes]);
    // bootstrap
    return angularAMD.bootstrap(app);
});

//加载config，调用默认的app模块
require(["config"]);