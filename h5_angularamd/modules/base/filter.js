define(["app"], function (app) {
    
    app.filter('prefix', function() {
      return function(input, prefix) {
        input = input || '';
        
        return input + "______" + prefix ;
      };
    });
    
	// controller
	return ["$scope", function ($scope) {
		$scope.title = "标题_";
	}];	
    
});