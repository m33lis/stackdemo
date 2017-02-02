(function() {
    'use strict';
    angular
        .module('stackServiceApp')
        .factory('Stacks', Stacks);

    Stacks.$inject = ['$resource'];

    function Stacks ($resource) {
        var resourceUrl =  'api/stacks/session/:session';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                },
                isArray: true
            },
            'update': { method:'PUT' }
        });
    }
})();
