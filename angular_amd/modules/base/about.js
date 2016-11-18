define(["layer"], function (layer) {
	
    layer.config({
        path:"/bower_components/layer/",
        extend: [
            'extend/layer.ext.js'
        ]
    });
    
	// controller
	return ["$scope", "$stateParams","$rootScope",function ($scope,$stateParams,$rootScope) {
		
        //状态变化
        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState,fromParams, options){
            console.warn("状态从:" ,fromState, "   转到:   " ,toState);
        });
        
        //无状态
        $rootScope.$on('$stateNotFound', 
            function(event, unfoundState, fromState, fromParams){ 
                console.log(unfoundState.to); // "lazy.state"
                console.log(unfoundState.toParams); // {a:1, b:2}
                console.log(unfoundState.options); // {inherit:false} + default options
            });
        
        //状态切换成功
        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
            console.info("$stateChangeSuccess");
        });
        
        //状态切换失败
        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams,error){
            console.info("$stateChangeError");
        });
        
        //内容加载中
        $rootScope.$on('$viewContentLoading', 
        function(event, viewConfig){ 
            // Access to all the view config properties.
            // and one special property 'targetView'
            // viewConfig.targetView 
            console.info("$viewContentLoading",viewConfig.targetView);
        });
        
        //内容加载结束
        $scope.$on('$viewContentLoaded', function(event){
            console.info("$viewContentLoaded",event);
        });
        
		// properties
	    $scope.title = "This is About page,参数为:" + $stateParams.aboutid;
        
        $scope.layerFunc = function(){
            layer.msg("ddddddddddd");
        }
	}];	
});