define(["echarts","directivedateranger","asyncLoader"], function (echarts,directivedateranger,asyncLoader) {
    // controller
    return ["$scope", function ($scope) {
        picrun_ini()

    // properties
        $scope.title = "这是标.....";

        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/display/ditu/show.html",
                viewModel:"/app/pages/display/ditu/show.js"
            });

        }


    }];

});