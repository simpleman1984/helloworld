define(["echarts","directivedateranger","asyncLoader"], function (echarts,directivedateranger,asyncLoader) {
    // controller
    return ["$scope", function ($scope) {
        picrun_ini()

    // properties
        $scope.title = "这是标.....";

    //builds
        $scope.builds =[
            {
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"1"
            },
            {
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"2"
            },{
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"3"
            },{
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"4"
            },{
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"5"
            },{
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"6"
            },{
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"7"
            },
            {
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"8"
            },
            {
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"9"
            },
            {
                id:1,
                img:'/app/pages/imgs/img03.jpg',
                name:'水城大酒店',
                label:"36kgce/m2",
                num:"10"
            }

            ];

        $scope.showModal = function(id){
            asyncLoader.showModal({
                view:"/app/pages/ditu/baseinfo.html?id="+id,
                viewModel:"/app/pages/ditu/baseinfo.js"
            });
        }
    }];
});