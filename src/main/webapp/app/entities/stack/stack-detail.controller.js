(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .controller('StackDetailController', StackDetailController);

    StackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Stacks'];

    function StackDetailController($scope, $rootScope, $stateParams, previousState, entity, Stack) {
        var vm = this;

        vm.stack = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('stackServiceApp:stackUpdate', function(event, result) {
            vm.stack = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
