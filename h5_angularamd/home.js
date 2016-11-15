define(["asyncLoader","show"], function (asyncLoader,show) {
	
    
	// controller
	return ["$scope", function ($scope) {
		
		// properties
	    $scope.title = "This is Home page";
        $scope.includeTitle = "This is The child home page!!!!";
        
        $scope.setValue = function(){
            show.setValue();
        }
        
        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"../show.html",
                viewModel:"../show.js"
            });
        }
	}];	
});