(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .controller('StackDialogController', StackDialogController);

    StackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stacks'];

    function StackDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Stack) {
        var vm = this;

        vm.stack = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stack.id !== null) {
                Stack.update(vm.stack, onSaveSuccess, onSaveError);
            } else {
                Stack.save(vm.stack, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('stackServiceApp:stackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
