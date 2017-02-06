(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .controller('StackController', StackController);

    StackController.$inject = ['$timeout', '$scope', '$state', 'Stack', 'stacks', 'orderByFilter', 'Auth', 'session', 'Sessions'];

    function StackController ($timeout, $scope, $state, Stack, stacks, orderBy, Auth, session, Sessions) {
        var vm = this;

        vm.stack = {};
        vm.stacks = stacks;


        vm.account = null;

        vm.save = save;
        vm.clear = clear;
        vm.logout = logout;
        vm.dragStopCallback = dragStopCallback;

        onControllerLoad();

        $timeout(function (){
            angular.element('input.stack-input').focus();
        });

        function onControllerLoad() {

            if (session) {
                vm.stack['session'] = session;
            } else {
                Sessions.getAll(function (res) {
                    vm.stack['session'] = _.last(res).series;
                    $state.go('stacks', {session: vm.stack.session});
                });
            }
        }

        function save () {
            if (vm.stack.val > 0) {
                vm.isSaving = true;
                Stack.save(vm.stack, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('stackServiceApp:stackUpdate', result);
            vm.isSaving = false;

            $timeout(function () {
                $state.go('stacks', null, { reload: 'stacks' });
            }, 100)
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function logout() {
            // cleanup first
            clear();
            Auth.logout();
            $state.go('home');
        }

        function clear() {
            _.each(vm.stacks, function (stack) {
                Stack.delete({id: stack.id});
            });
            $timeout(function () {
                $state.go('stacks', null, { reload: 'stacks' });
            }, 100);
        }

        function dragStopCallback() {
            $state.go('stacks.delete', {id: _.last(vm.stacks).id});
        }
    }
})();
