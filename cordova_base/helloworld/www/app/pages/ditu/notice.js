define(["angular","echarts","directivedateranger","directiveicheck","directivedaters"], function (angular,echarts,directivedateranger,directiveicheck,directivedaters,app) {
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