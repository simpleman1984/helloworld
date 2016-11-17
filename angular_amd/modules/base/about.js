define(["layer"], function (layer) {
	
    layer.config({
        path:"/bower_components/layer/",
        extend: [
            'extend/layer.ext.js'
        ]
    });
    
	// controller
	return ["$scope", "$transition",function ($scope,$transition$) {
		
		// properties
	    $scope.title = "This is About page,参数为:" + $transition$.params().aboutid;
        
        $scope.layerFunc = function(){
            layer.msg("ddddddddddd");
        }
	}];	
});