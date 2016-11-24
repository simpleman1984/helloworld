define(["asyncLoader"], function (asyncLoader) {

	// controller
	return ["$scope", function ($scope) {

		// properties
	    $scope.title = "This is Home page";

        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/demo/show.html",
                viewModel:"/app/pages/demo/show.js"
            });
        }
	}];
});