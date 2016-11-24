define(["app", "textareas"], function (app, textareas) {

    app.directive('textareas', function() {
        var directive = {};

        directive.restrict = 'E';
        directive.template = "<textarea id='compose-textarea' class='form-control' style='height:300px'></textarea>";

        directive.link = function($scope, element, attributes) {
            $(element).textareas();
        };

            //Add text editor
            $("#compose-textarea").wysihtml5();

        return directive;
    });

    return {};
});