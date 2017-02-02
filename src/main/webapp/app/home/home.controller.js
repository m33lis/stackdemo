(function() {
    'use strict';

    angular
        .module('stackServiceApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$rootScope', '$scope', 'Principal', 'LoginService', '$state', 'Auth', 'Sessions'];

    function HomeController ($rootScope, $scope, Principal, LoginService, $state, Auth, Sessions) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.sessions = null;

        vm.register = register;
        vm.goGoGo = goGoGo;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function goGoGo(event) {
            // helper function to facilitate auto-login
            event.preventDefault();
            Auth.login({username: "user",
                        password: "user",
                        rememberMe: true
            }).then(function () {
                getSessionsAndGo();
            }).catch(function () {
                console.log("Oops, auth error!");
            });
        }

        function getSessionsAndGo() {
            Sessions.getAll(function (result) {
                vm.sessions = result;
                console.log("Sending the following session:: ", _.last(vm.sessions).series);
                $state.go('stacks', { session: _.last(vm.sessions).series });
                $rootScope.$broadcast('authenticationSuccess');
            });
        }

    }
})();
