//普通方法测试
describe("A suite of basic functions", function() {
    it("reverse wosrd",function(){
        expect("DCBA").toEqual(reverse("ABCD"));
        expect("Conan").toEqual(reverse("nanoC"));
    });
});

//测试require
require(["module_test_deps"],function(module_test_deps){
    alert(module_test_deps);
    
    describe('just checking1', function() {
        module_test_deps.hello();
    });
    describe('just checking21', function() {
    });

});