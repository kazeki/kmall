(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('SystemNoticeDialogController', SystemNoticeDialogController);

    SystemNoticeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SystemNotice', 'User'];

    function SystemNoticeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SystemNotice, User) {
        var vm = this;

        vm.systemNotice = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.systemNotice.id !== null) {
                SystemNotice.update(vm.systemNotice, onSaveSuccess, onSaveError);
            } else {
                SystemNotice.save(vm.systemNotice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kmallApp:systemNoticeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
