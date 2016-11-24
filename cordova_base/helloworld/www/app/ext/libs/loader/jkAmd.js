define([], function () {

    function JkAmd(){

    }

    JkAmd.prototype.route = function(url,templateURL,controllerURL){
        var _config = {
            url: "/home",
            templateUrl: templateURL,
            controller: [
                '$scope', '__AAMDCtrl', '$injector',
                function ($scope, __AAMDCtrl, $injector) {
                    if (typeof __AAMDCtrl !== 'undefined' ) {
                        $injector.invoke(__AAMDCtrl, this, { '$scope': $scope });
                    }
                }
            ],
            resolve: {
                __AAMDCtrl: ['$q', '$rootScope', function ($q, $rootScope) { // jshint ignore:line
                    var defer = $q.defer();
                    require([controllerURL], function (ctrl) {
                        defer.resolve(ctrl);
                        $rootScope.$apply();
                    });
                    return defer.promise;
                }]
            },
        };
        return _config;
    };

    return new JkAmd();
});