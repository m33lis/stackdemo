(function() {
    'use strict';
    angular
        .module('stackServiceApp')
        .factory('Stack', Stack);

    Stack.$inject = ['$resource'];

    function Stack ($resource) {
        var resourceUrl =  'api/stacks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
