requirejs.config({
    shim:{
        "module2":{
            /** makeShimExports 严重参考该方法 **/
            
            exports:"xx",
            init: function() {
                return {
                  hello: hello,
                  xx:xx
                }
            }
        }
    }
});
define(["module1","module2",/*"css!app"*/],function(module1,module2){
    console.info(module1,module2);
});
define("x",["module1","module2",/*"css!app"*/],function(module1,module2){
    console.info(module1,module2);
});