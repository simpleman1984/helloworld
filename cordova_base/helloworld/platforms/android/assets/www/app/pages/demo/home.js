define(["echarts","directivedateranger","directiveicheck","directivejstree","directivedaters","asyncLoader"], function (echarts,directivedateranger,directiveicheck,directivejstree,directivedaters,asyncLoader) {

    // controller
        return ["$scope", function ($scope) {

		// properties
	    $scope.title = "这是标";

        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/demo/show.html",
                viewModel:"/app/pages/demo/show.js"
            });

        }
	}];
});