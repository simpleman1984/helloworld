define(["app", "directivejstree","daterangepicker","directiveicheck"], function (app, directivejstree,daterangepicker,directiveicheck) {
    app.directive('directivedaters', function() {
        var directive = {};

        directive.restrict = 'E';
        directive.templateUrl = "/app/ext/libs/angularjsdirective/fenxi/fenxi.html";
        directive.replace =true;
        directive.link = function($scope, element, attributes) {
            console.log("ffffffffffffffffff");
        };

        return directive;
    });

    return {};
});