(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('GoodsDetailController', GoodsDetailController);

    GoodsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Goods', 'Shop', 'User'];

    function GoodsDetailController($scope, $rootScope, $stateParams, previousState, entity, Goods, Shop, User) {
        var vm = this;

        vm.goods = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kmallApp:goodsUpdate', function(event, result) {
            vm.goods = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
