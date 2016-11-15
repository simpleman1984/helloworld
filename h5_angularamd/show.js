define([], function () {
	console.info("show.js 初始化");
    
	//controller
	var result =  [];	
    
    var $scope ;
    function Callback (_$scope) {
        $scope = _$scope;
        
		$scope.title = "This is Home page --- " + new Date().getTime();
        $scope.result= 1;
        
        $scope.add = function(deleta){
            $scope.result += deleta;
        }
	};
    
    result.setValue = function(){
        console.info("34333333333333333",result);
        //设置属性
        $scope.title = "34333333333333333------";
        //调用方法
        $scope.add(2);
        //遍历触发方法生效
        $scope.$digest();
    };
    result.push("$scope");
    result.push(Callback);
    
    return result;
});