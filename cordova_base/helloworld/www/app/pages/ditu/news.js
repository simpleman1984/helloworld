define(["angular","directivedateranger","directiveicheck","directivedaters","directivetextarea"], function (angular,directivedateranger,directiveicheck,directivedaters,directivetextarea,app) {
    // controller
    return ["$scope", function ($scope) {


        // properties
        $scope.title = "这是标.....";
        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/display/ditu/show.html",
                viewModel:"/app/pages/display/ditu/show.js"
            });

        };



    }];

});