define(["echarts","directivedateranger","asyncLoader"], function (echarts,directivedateranger,asyncLoader) {
    // controller
    return ["$scope", function ($scope) {
        picrun_ini()

    // properties
        $scope.title = "这是标.....";


        //build
        $scope.build={
            name:'王府大酒店',
            function:'餐饮、住宿、会议、娱乐',
            year:'2004',
            character:'365*24小时，无休息',
            other:'涉外五星',
            people:'员工330、最大接待2000',
            sl:'32kgce/m2.y',
            rate:'5.2%'
        };

        $scope.showModal = function(){
            asyncLoader.showModal({
                view:"/app/pages/ditu/details.html",
                viewModel:"/app/pages/ditu/details.js"
            });

        }


    }];

});