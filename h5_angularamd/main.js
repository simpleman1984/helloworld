require.config({
    baseUrl:"/",
    paths: {
        // angular
        "angular": "/bower_components/angular/angular",
        
        // angular-ui
        "angular-ui-router": "/bower_components/angular-ui-router/release/angular-ui-router",
        
        // angularAMD
        "angularAMD": "/bower_components/angularAMD/angularAMD",
        "ngload": "/bower_components/angularAMD/ngload",
        "layer":"/bower_components/layer/layer",
        "asyncLoader":"/bower_components/loader/asyncLoader",
        
        //text
        "text":"/bower_components/requirejs/text",
        "bootstrap":"/bower_components/bootstrap/js/bootstrap",
        
        "show":"modules/base/show"
    },
    shim: {        
        // angular
		"angular": { exports: "angular" },
        
        // angular-ui
        "angular-ui-router": ["angular"],
        
        // angularAMD
        "angularAMD": ["angular"],
        "ngload": ["angularAMD"]
    },
    deps:["app"]
});