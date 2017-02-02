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
                stacks: ['$stateParams', 'Stacks', 'Stack',  function ($stateParams, Stacks, Stack) {
                    if ($stateParams.session) {
                        return Stacks.get({session: $stateParams.session}).$promise;
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
            onEnter: ['$stateParams', '$state', 'Stack', function($stateParams, $state, Stack) {
                Stack.delete({id: $stateParams.id});
                $state.go('stacks', null, { reload: 'stacks' });
            }]
        })
    }

})();
