define(["layer"], function (layer) {
	
    layer.config({
        path:"/bower_components/layer/",
        extend: [
            'extend/layer.ext.js'
        ]
    });
    
	// controller
	return ["$scope", function ($scope) {
		
		// properties
	    $scope.title = "This is About page";
        
        $scope.layerFunc = function(){
            layer.msg("ddddddddddd");
        }
	}];	
});