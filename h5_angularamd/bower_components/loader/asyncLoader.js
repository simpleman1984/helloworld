define(["layer"], function (layer) {
    
    //初始化layer加载路径~
    layer.config({
      extend: [
        'extend/layer.ext.js'
      ]
    });
    
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

    /**
     *显示弹出对话框
     */
    showModal = function(params) {
        var view = params.view;
        var viewModel = params.viewModel;

        var callback = params.callback || function(layero, index){};
        var width    = params.width    || "420px";
        var height   = params.height  || "240px";
        var title    = params.title || "信息";

        var uuid = UUID();
        //异步加载对应的html和js
        require([''+viewModel,"text!"+view], function (viewModel,view){
                $(view).appendTo("body")
                    .attr("id",uuid)
                    .attr("ng-controller",uuid);

                layer.open({
                    title:title,
                    type : 1,
                    skin : 'layui-layer-rim', //加上边框
                    area : [width,height], //宽高
                    content : $("#" + uuid),
                    success : function(){
                        //定义controller
                        app.controller(uuid,viewModel); 

                        //注册controller，插入element
                        //moduleName controllerName templateHtml
                        registerController("app",uuid,view);
                        callback();
                    }
                });
        });
    }; 
    
    //此处貌似不能返回普通的 对象 ，直接返回一个 function，question是可以的 
    var ret = function(){};
    
    ret.showModal = showModal;
    
    return ret;
});