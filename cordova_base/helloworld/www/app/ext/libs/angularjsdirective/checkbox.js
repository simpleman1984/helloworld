define(["app", "icheck"], function (app, icheck) {
    app.directive('icheck', function() {
        var directive = {};

        directive.restrict = 'E';
        directive.template = "<input type='radio' class='flat-red' name='r1' checked >";
        directive.link = function($scope, element, attributes) {
          //  $(element).icheck();

            $(function () {
                //iCheck for checkbox and radio inputs
                $('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
                    checkboxClass: 'icheckbox_minimal-blue',
                    radioClass: 'iradio_minimal-blue'
                });
                //Red color scheme for iCheck
                $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
                    checkboxClass: 'icheckbox_minimal-red',
                    radioClass: 'iradio_minimal-red'
                });
                //Flat red color scheme for iCheck
                $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
                    checkboxClass: 'icheckbox_flat-blue',
                    radioClass: 'iradio_flat-blue'
                });
            });
        };

        return directive;
    });

    return {};
});