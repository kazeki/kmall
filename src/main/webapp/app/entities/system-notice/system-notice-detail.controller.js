(function() {
    'use strict';

    angular
        .module('kmallApp')
        .controller('SystemNoticeDetailController', SystemNoticeDetailController);

    SystemNoticeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SystemNotice', 'User'];

    function SystemNoticeDetailController($scope, $rootScope, $stateParams, previousState, entity, SystemNotice, User) {
        var vm = this;

        vm.systemNotice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kmallApp:systemNoticeUpdate', function(event, result) {
            vm.systemNotice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
