(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .controller('StackController', StackController);

    StackController.$inject = ['$timeout', '$scope', '$state', 'Stack', 'Stacks', 'stacks',  'orderByFilter', 'Auth', 'session', 'Sessions', '$q'];

    function StackController ($timeout, $scope, $state, Stack, Stacks, stacks, orderBy, Auth, session, Sessions, $q) {
        var vm = this;

        vm.stacks = stacks;
        vm.stack = {};

        if (session && stacks) {
            vm.stack['session'] = session;
        } else {
            Sessions.getAll(function (res) {
                vm.stack['session'] = _.last(res).series;
                $state.go('stacks', {session: vm.stack.session});
            });
        }

        vm.account = null;

        vm.save = save;
        vm.clear = clear;
        vm.logout = logout;

        loadAll();

        $timeout(function (){
            angular.element('input.val').focus();
        });

        function loadAll() {
            /*Stacks.get(function(result) {
                vm.stacks = result;
            });
*/
            console.log("CURRENT SESSION: ", vm.stack.session);
        }

        function save () {
            vm.isSaving = true;
            Stack.save(vm.stack, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('stackServiceApp:stackUpdate', result);
            vm.isSaving = false;
            $state.go('stacks', null, { reload: 'stacks' });
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function logout() {
            Auth.logout();
            $state.go('home');
        }

        function clear() {
            _.each(vm.stacks, function (stack) {
                Stack.delete({id: stack.id});
            });
            $state.go('stacks', null, { reload: 'stacks' });
        }


    }
})();
