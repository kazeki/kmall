(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('GoodsDeleteController',GoodsDeleteController);

    GoodsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Goods'];

    function GoodsDeleteController($uibModalInstance, entity, Goods) {
        var vm = this;

        vm.goods = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Goods.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
