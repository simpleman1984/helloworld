define(["asyncLoader","show"], function (asyncLoader,show) {
	
    
	// controller
	return ["$scope", function ($scope) {
		
		// properties
	    $scope.title = "This is Home page";
        $scope.includeTitle1 = "这里是第一个选项卡";
        $scope.includeTitle2 = "这里是第二个选项卡";
        
        $scope.setValue = function(){
            show.setValue();
        }
        
        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"show.html",
                viewModel:"modules/base/show.js"
            });
        }
        
        $scope.templates =
            [{ name: 'template1.html', url: 'modules/base/home/home_include1.html'},
             { name: 'template2.html', url: 'modules/base/home/home_include2.html'}];
        $scope.template = $scope.templates[0];
	}];	
});