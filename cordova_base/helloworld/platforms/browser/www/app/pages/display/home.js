define([], function () {

    // controller
    return ["$scope", function ($scope) {
        // properties
        $scope.title = "This is About page";

        $scope.layerFunc = function(){
            layer.msg("ddddddddddd");
        }
    }];
});