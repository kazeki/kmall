(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$q', 'Shop'];

    function HomeController ($scope, Principal, LoginService, $state, $q, Shop) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
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
        vm.promiseTest = promiseTest;
        function promiseTest () {
            var deferedA = $q.defer();
            var promiseA = deferedA.promise;
            var deferedB = $q.defer();
            var promiseB = deferedB.promise;
            var promiseC = $q.all([promiseA,promiseB]);
            promiseA.then(function(value){
                console.log("in promiseA ---- success");
                console.log(value);
                return value;
            }).then(function(value){
                console.log("in promiseA ---- success2");
                console.log(value);
                return value;
            }).finally(function(){
                console.log('in promiseA ---- finally');
            });
            promiseB.then(function(value){
                console.log("in promiseB ---- success");
                console.log(value);
                return value;
            }).then(function(value){
                console.log("in promiseB ---- success2");
                console.log(value);
                return value;
            }).finally(function(){
                console.log('in promiseB ---- finally');
            });
            promiseC.then(function(value){
                console.log("in promiseC ---- success");
                console.log(value);
                return value;
            }).then(function(value){
                console.log("in promiseC ---- success2");
                console.log(value);
                return value;
            }).finally(function(){
                console.log('in promiseC ---- finally');
            });

            var a = 100;
            var b = 90;
            function runA(){
                console.log('>>>>>>>>>> A:' + a);
                if(a>0){
                    a--;
                    setTimeout(runA, 100);
                }else{
                    deferedA.resolve("OK A");
                }
            }
            function runB(){
                console.log('>>>>>>>>>> B:' + b);
                if(b>0){
                    b--;
                    setTimeout(runB, 100);
                }else{
                    deferedB.resolve("OK B");
                }
            }
            runA();
            runB();
            console.log('>>>>>>>>>> started');




        }


    }
})();
