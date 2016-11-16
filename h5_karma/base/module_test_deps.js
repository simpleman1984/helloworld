define(["module_test","exports"],function(module_test,exports) {

    var deps = module_test;
    
    exports.hello = function(){
        return "module deps : "+deps;   
    }
});