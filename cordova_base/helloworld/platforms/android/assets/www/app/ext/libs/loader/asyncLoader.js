define(["layer", "app"], function (layer, app) {
    //初始化layer加载路径~
    layer.config({
        path: "/app/ext/libs/layer/",
        extend: [
            'extend/layer.ext.js'
        ]
    });

    //生成 UUID方法
    var UUID = (function (uuidRegEx, uuidReplacer) {
        return function () {
            return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(uuidRegEx, uuidReplacer).toUpperCase();
        };
    })(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == "x" ? r : (r & 3 | 8);
        return v.toString(16);
    });

    function registerController(moduleName, controllerName, template, container) {
        var queue = angular.module(moduleName)._invokeQueue;
        for (var i = 0; i < queue.length; i++) {
            var call = queue[i];
            if (call[0] == "$controllerProvider" &&
                call[1] == "register" &&
                call[2][0] == controllerName) {
                getApp().$controllerProvider.register(controllerName, call[2][1]);
            }
            ;
        }
        ;

        //以下这个写法，不管怎么修改均会报错
        //var $injector = angular.injector(['ng', 'app']);
        //$injector.invoke(function($rootScope, $compile) {
        //    $compile(element)($rootScope);
        //});

        //如下写法，解决了大问题！
        var $injector = angular.element($('body')).injector();
        $injector.invoke(function ($compile, $rootScope) {
            var $element = $('#' + controllerName);
            var scope = angular.element($element).scope();
            $compile($element)(scope);
            scope.$apply();
        });
    };

    /**
     *显示弹出对话框
     */
    showModal = function (params) {
        var view = params.view;
        var viewModel = params.viewModel;

        var callback = params.callback || function (layero, index) {
            };
        var width = params.width || "60%";
        var height = params.height || "500px";
        var title = params.title || "建筑排名";
        var uuid = UUID();

        //异步加载对应的html和js
        require(['' + viewModel, "text!" + view], function (viewModel, view) {
            $(view).appendTo("body")
                .attr("id", uuid)
                .attr("ng-controller", uuid);

            layer.open({
                title: title,
                type: 1,

                area: [width, height], //宽高
                content: $("#" + uuid),
                success: function () {
                    //定义controller
                    getApp().controller(uuid, viewModel);

                    //注册controller，插入element
                    //moduleName controllerName templateHtml
                    registerController("app", uuid, view);
                    callback();
                }
            });
        });
    };

    function getApp(){
        return !app?window.app:app;
    };

    //此处貌似不能返回普通的 对象 ，直接返回一个 function，question是可以的 
    var ret = function () {
    };

    ret.showModal = showModal;

    return ret;
});