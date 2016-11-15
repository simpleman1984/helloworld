

define(["app"], function (app) {
    //定义service基础示例
    app.directive('myCustomer', function() {
      return {
        templateUrl: function(elem, attr) {
          return '/modules/base/directive'+attr.type+'.html';
        },
          /**
          https://docs.angularjs.org/guide/directive
          'A' - only matches attribute name
'E' - only matches element name
'C' - only matches class name
'M' - only matches comment*/
        restrict: 'A',
        scope: {
          /** 将参数customer转换成别名customerInfo **/
          customerInfo: '=customer'
        },
        link:function(scope, element, attrs) {
          
        },
        transclude: true
      };
    });
    
    var MyController = function($scope, notify) {
        $scope.customer = {name:"名称~~~",address:"地址!!!!"};
        
        $scope.title = "标签示例页面";
    };
	MyController.$inject = ['$scope'];
    
    return MyController;
});