define(["app", "daterangepicker"], function (app, daterangepicker) {

    app.directive('daterangepicker', function() {
        var directive = {};

        directive.restrict = 'E';
        directive.template = "<input type='text' name='daterange' class='form-control' value='01/01/2015 - 01/31/2015' />";

        directive.link = function($scope, element, attributes) {
            $(element).daterangepicker();
        };

        return directive;
    });

    return {};
});


