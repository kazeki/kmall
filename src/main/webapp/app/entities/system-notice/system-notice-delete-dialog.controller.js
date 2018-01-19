(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('SystemNoticeDeleteController',SystemNoticeDeleteController);

    SystemNoticeDeleteController.$inject = ['$uibModalInstance', 'entity', 'SystemNotice'];

    function SystemNoticeDeleteController($uibModalInstance, entity, SystemNotice) {
        var vm = this;

        vm.systemNotice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SystemNotice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
