require.config({
    baseUrl:"bower_components",
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
        
        "show":"../show"
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