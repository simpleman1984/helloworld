//必须注意下面的顺序
define("config", ["/app/ext/libs/requirejs/config.js"]);
//定义总入口
define("app", ["angular", "angularAMD", "angular-ui-router"], function (angular, angularAMD) {

    // routes
    var registerRoutes = function ($stateProvider, $urlRouterProvider) {

        // default
        $urlRouterProvider.otherwise("/datamonitor");

        // route
        $stateProvider.state("home", angularAMD.route({
            url: "/home",
            templateUrl: "display/home.html",
            controllerUrl: "./display/home.js"
        }));

        $stateProvider.state("datamonitor", angularAMD.route({
            url: "/datamonitor",
            templateUrl: "display/query/datamonitor.html",
            controllerUrl: "./display/query/datamonitor.js"
        }));

        $stateProvider.state("dataquery", angularAMD.route({
            url: "/dataquery",
            templateUrl: "display/query/dataquery.html",
            controllerUrl: "./display/query/dataquery.js"
        }));

        $stateProvider.state("maps", angularAMD.route({
            url: "/maps",
            templateUrl: "ditu/maps.html",
            controllerUrl: "./display/query/maps.js"
        }));

        $stateProvider.state("compares", angularAMD.route({
            url: "/compares",
            templateUrl: "display/query/compares.html",
            controllerUrl: "./display/query/compares.js"
        }));

        $stateProvider.state("reports", angularAMD.route({
            url: "/reports",
            templateUrl: "display/query/reports.html",
            controllerUrl: "./display/query/reports.js"
        }));

        $stateProvider.state("judges", angularAMD.route({
            url: "/judges",
            templateUrl: "display/query/judges.html",
            controllerUrl: "./display/query/judges.js"
        }));
    };

    // module
    var app = angular.module("app", ["ui.router"], function ($controllerProvider) {
        window.controllerProvider = $controllerProvider;
    });

    //顶部的控制器
    app.controller('headerCtrl', function ($scope) {

        //代办消息
        var msgList = [];
        msgList[0] = {title: "下午2点开会", content: "下午2点开会", url: "http://www.baidu.com"};
        $scope.msgList = msgList;

        //警告消息
        var alertList = [];
        alertList[0] = {title: "采集器1工作异常", content: "采集器1工作异常", url: "http://www.baidu.com"};
        $scope.alertList = alertList;

        //当前生效的建筑名
        $scope.buildName = "天目湖宾馆";
    });
    //左侧的菜单控制器
    app.controller('menuCtrl', function ($scope) {
        $scope.menus = [
            {
                name: "能耗监控",
                items: [
                    {
                        name: "实时监控",
                        url: "#datamonitor"
                    },
                    {
                        name: "能耗查询",
                        url: "#dataquery"
                    },
                    {
                        name: "对比分析",
                        url: "#compares"
                    },
                    {
                        name: "能耗报表",
                        url: "#reports"
                    }
                ]
            },
            {
                name: "数据分析",
                items: [
                    {
                        name: "能耗诊断",
                        url: "#judges"
                    },
                    {
                        name: "表计读数",
                        url: "#"
                    },
                    {
                        name: "拆分分析",
                        url: "#"
                    },
                    {
                        name: "仪表参数分析",
                        url: "#"
                    },
                    {
                        name: "原始XML分析",
                        url: "#"
                    }
                ]
            },
            {
                name: "大型设备",
                items: [
                    {
                        name: "空调监测",
                        url: "#"
                    }
                ]
            },
            {
                name: "集团功能",
                items: [
                    {
                        name: "地图",
                        url: "#maps"
                    },
                    {
                        name: "建筑汇总",
                        url: "#"
                    },
                    {
                        name: "建筑排名",
                        url: "#"
                    }
                ]
            },
            {
                name: "系统维护",
                items: [
                    {
                        name: "建筑管理",
                        url: "#"
                    },
                    {
                        name: "建筑部位维护",
                        url: "#"
                    },
                    {
                        name: "建筑采集器维护",
                        url: "#"
                    },
                    {
                        name: "监测仪表产品维护",
                        url: "#"
                    },
                    {
                        name: "采集器监测仪表维护",
                        url: "#"
                    },
                    {
                        name: "分类分项能耗编码",
                        url: "#"
                    },
                    {
                        name: "建筑分类分项能耗",
                        url: "#"
                    },
                    {
                        name: "建筑能耗分时单价",
                        url: "#"
                    },
                    {
                        name: "部位拓扑关系",
                        url: "#"
                    },
                    {
                        name: "首页指标维护",
                        url: "#"
                    },
                    {
                        name: "自助界面配置",
                        url: "#"
                    },
                    {
                        name: "空调监测配置",
                        url: "#"
                    },
                    {
                        name: "冷热量仪充值页面",
                        url: "#"
                    },
                    {
                        name: "用户权限配置",
                        url: "#"
                    }
                ]
            }
        ];
    });
    //对外暴露当前页面
    window.app = app;
    // config
    app.config(["$stateProvider", "$urlRouterProvider", registerRoutes]);
    // bootstrap
    return angularAMD.bootstrap(app);
});
//加载config，调用默认的entry模块
require(["config"]);