(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('ShopDetailController', ShopDetailController);

    ShopDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Shop', 'User'];

    function ShopDetailController($scope, $rootScope, $stateParams, previousState, entity, Shop, User) {
        var vm = this;

        vm.shop = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kmallApp:shopUpdate', function(event, result) {
            vm.shop = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
