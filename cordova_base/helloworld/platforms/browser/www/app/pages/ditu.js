//必须注意下面的顺序
define("config", ["/app/ext/libs/requirejs/config.js"]);
//定义总入口
define("app", ["angular","angularAMD","asyncLoader", "angular-ui-router","select2"], function (angular, angularAMD,asyncLoader,select2) {

    //建筑下拉
  $(function () {
        $(".select2").select2();

          //Add text editor
    });



    // routes
    var registerRoutes = function ($stateProvider, $urlRouterProvider) {

        // default
        $urlRouterProvider.otherwise("/maps");

        // route
        $stateProvider.state("maps", angularAMD.route({
            url: "/maps",
            templateUrl: "/app/pages/ditu/maps.html",
            controllerUrl: "/app/pages/ditu/maps.js"
        }));

        $stateProvider.state("news", angularAMD.route({
            url: "/news",
            templateUrl: "/app/pages/ditu/news.html",
            controllerUrl: "/app/pages/display/ditu/news.js"
        }));
    };
        // module
    var app = angular.module("app", ["ui.router"], function ($controllerProvider) {
        window.controllerProvider = $controllerProvider;
    });

    // config
    app.config(["$stateProvider", "$urlRouterProvider", registerRoutes]);

    var MODALTYPE ={
        SORT:'SORT', ANASYS:'ANASYS',PUBLISH:'PUBLISH',INFO:'INFO',ALARM:'ALARM'
    };

    //左侧菜单控制器
    app.controller("leftCtl",function($scope){

        $scope.builds=[
            {
            template:'常州',build:[
                {id:1,name:'1常州天目湖酒店'},
                {id:2,name:'2常州天目湖酒店'},
                {id:3,name:'3常州天目湖酒店'}
            ]
            },
            {
                template:'天目湖',build:[
                {id:4,name:'A常州天目湖酒店'},
                {id:5,name:'B常州天目湖酒店'},
                {id:6,name:'C常州天目湖酒店'}
            ]
            }];

        $scope.showModal = function(modalType){
            if(modalType == MODALTYPE.SORT){
                asyncLoader.showModal({
                    view:"/app/pages/ditu/level.html",
                    viewModel:"/app/pages/ditu/level.js"
                });}
            else if(modalType == MODALTYPE.ANASYS){
                asyncLoader.showModal({
                    view:"/app/pages/ditu/nhfx.html",
                    viewModel:"/app/pages/ditu/nhfx.js"
                });}
            else if(modalType == MODALTYPE.PUBLISH){
                asyncLoader.showModal({
                    view:"/app/pages/ditu/news.html",
                    viewModel:"/app/pages/ditu/news.js"
                });}
            else if(modalType == MODALTYPE.INFO){
                asyncLoader.showModal({
                    view:"/app/pages/ditu/notice.html",
                    viewModel:"/app/pages/ditu/notice.js"
                });}
            else if(modalType == MODALTYPE.ALARM){
                asyncLoader.showModal({
                    view:"/app/pages/ditu/alarm.html",
                    viewModel:"/app/pages/ditu/alarm.js"
                });}
        };
    });

// bootstrap
    var _app = angularAMD.bootstrap(app);
    //对外暴露当前页面
    window.app = _app;
    return _app;
});
//加载config，调用默认的app模块
require(["config"]);
