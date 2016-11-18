var app = angular.module("app",[]);
app.controller("ctl",["$scope",function($scope){
    $scope.val = "";
    $scope.urlList = [];
    $scope.urlSize = 0;
    
    //请求的内容（后期修改为保存到文件）
    var requestContent = {};
    
    $scope.clear = function(){
        $scope.urlList = [];
        $scope.urlSize = 0;
        requestContent={};
    };
    
    $scope.$watch('val', function(newValue, oldValue) {
      $scope.$digest();
    });
    
    //复制文本
    $scope.copyText = function(url){
        document.getElementById("text").value = requestContent[url];
       //Get Input Element
        document.getElementById("text").select();

        //Copy Content
        document.execCommand("Copy", false, null);
        alert("copy success");
    };
    
    //查询
    $scope.search = function(item){
        var url = item.url;
        var content = requestContent[url];
        if($scope.val == "")
        {
            return true;
        }
        if(content && content.indexOf($scope.val) > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    };
    
    chrome.devtools.network.onRequestFinished.addListener(
      function(request) {
        //强制刷新页面
        $scope.$digest();
        request.getContent(function(c){
            requestContent[request.request.url] = c;
        });
        $scope.urlList.push({url:request.request.url});
        $scope.urlSize = $scope.urlList.length;
//        if (request.response.bodySize > 1024*1024) {
//          chrome.devtools.inspectedWindow.eval(
//              'console.log("Large image: " + unescape("' +
//              escape(request.request.url) + '"))');
//        }
    });
    
}]);
