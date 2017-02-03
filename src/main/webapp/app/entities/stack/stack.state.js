(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider

        .state('stacks', {
            parent: 'entity',
            url: '/stacks',
            params: {
                session: null
            },
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Stacks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stack/stacks.html',
                    controller: 'StackController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                stacks: ['$stateParams', 'Stacks',  function ($stateParams, Stacks) {
                    if ($stateParams.session) {
                        return Stacks.get({session: $stateParams.session});
                    }
                }],
                session: ['$stateParams', function ($stateParams) {
                    return $stateParams.session;
                }]
            }
        })
        .state('stacks.delete', {
            parent: 'stacks',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', 'Stack', '$timeout', function($stateParams, $state, Stack, $timeout) {
                Stack.delete({id: $stateParams.id});
                $timeout(function () {
                    // ze ugly hack :/
                    $state.go('stacks', null, {reload: 'stacks' });
                }, 100);

            }]
        })
    }

})();
