//requirejs配置
require.config({
    baseUrl:"/",
    paths: {
        // angular
        "angular": "app/ext/libs/angularjs/angular",
        // angular-ui
        "angular-ui-router": "app/ext/libs/angular-ui-router/angular-ui-router",
        // angularAMD
        "angularAMD": "app/ext/libs/angularAMD/angularAMD",
        //jQuery
        "jquery":"plugins/jQuery/jquery-2.2.3.min",
        //text
        "text":"app/ext/libs/requirejs/text",

        //插件
        "asyncLoader":"app/ext/libs/loader/asyncLoader",
        "jkAmd": "app/ext/libs/loader/jkAmd",
        "layer":"app/ext/libs/layer/layer",
        "echarts": "app/ext/libs/echarts/echarts.min",
        "moment":"/plugins/daterangepicker/moment",
        "ol":"app/ext/libs/openlayers/ol",
        "select2":"/plugins/select2/select2.full.min",



        //时间区间选择
        "daterangepicker":"/plugins/daterangepicker/daterangepicker",
        "directivedateranger":"app/ext/libs/angularjsdirective/dateranger",

        // 单选按钮样式//
         "icheck":"/plugins/icheck/icheck",
        "directiveicheck":"app/ext/libs/angularjsdirective/checkbox",

        // 富文本//
         "textareas":"/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min",
         "directivetextarea":"app/ext/libs/angularjsdirective/textarea",

        //jstree 树型结构
        "jstree":"/plugins/jstree/jstree.min",
        "directivejstree":"app/ext/libs/angularjsdirective/jstree",

        //数据分析
        "directivedaters":"app/ext/libs/angularjsdirective/fenxi/fenxi",

        //angularjs的选择directive

        "map":"app/ext/libs/angularjsdirective/map"
    },
    shim: {
        // angular
        "angular": { exports: "angular" },

        // angular-ui
        "angular-ui-router": ["angular"],

        // angularAMD
        "angularAMD": ["angular"],

        //jQuery
        "layer":{
            deps:["jquery"]
        },

        //icheck 依赖jquery
        "icheck":{
            deps:["jquery"]
        },

        //app 默认需要加载app
        "app":["angular"]
    },
    deps:["app"]
});