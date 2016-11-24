define(["app", "jstree"], function (app, jstree) {
    app.directive('jstree', function () {
        var directive = {};

        directive.restrict = 'E';
        directive.template = "<div id='using_json'></div>";

        directive.link = function ($scope, element, attributes) {
            $(element).jstree({
                'core': {
                    'data': [
                        '第一级',
                        {
                            'text': '第二级',
                            'state': {
                                'opened': false
                            },
                            'children': [
                                {
                                    'text': '111222',
                                    'children': [
                                        {
                                            'text': 'animate.css',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'bootstrap.css',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'main.css',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'style.css',
                                            'icon': 'none'
                                        }
                                    ],
                                    'state': {
                                        'opened': false
                                    }
                                },
                                {
                                    'text': 'js',
                                    'children': [
                                        {
                                            'text': 'bootstrap.js',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'hplus.min.js',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'jquery.min.js',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'jsTree.min.js',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'custom.min.js',
                                            'icon': 'none'
                                        }
                                    ],
                                    'state': {
                                        'opened': false
                                    }
                                },
                                {
                                    'text': 'html',
                                    'children': [
                                        {
                                            'text': 'layout.html',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'navigation.html',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'navbar.html',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'footer.html',
                                            'icon': 'none'
                                        },
                                        {
                                            'text': 'sidebar.html',
                                            'icon': 'none'
                                        }
                                    ],
                                    'state': {
                                        'opened': false
                                    }
                                }
                            ]
                        },
                        'Fonts',
                        'Images',
                        'Scripts',
                        'Templates'
                    ]
                }
            });

        };

        return directive;
    });

    return {};
});