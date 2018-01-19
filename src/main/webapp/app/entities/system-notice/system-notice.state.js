(function() {
    'use strict';

    angular
        .module('kmallApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('system-notice', {
            parent: 'entity',
            url: '/system-notice?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.systemNotice.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-notice/system-notices.html',
                    controller: 'SystemNoticeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('systemNotice');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('system-notice-detail', {
            parent: 'system-notice',
            url: '/system-notice/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.systemNotice.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/system-notice/system-notice-detail.html',
                    controller: 'SystemNoticeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('systemNotice');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SystemNotice', function($stateParams, SystemNotice) {
                    return SystemNotice.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'system-notice',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('system-notice-detail.edit', {
            parent: 'system-notice-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-notice/system-notice-dialog.html',
                    controller: 'SystemNoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemNotice', function(SystemNotice) {
                            return SystemNotice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-notice.new', {
            parent: 'system-notice',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-notice/system-notice-dialog.html',
                    controller: 'SystemNoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                title: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('system-notice', null, { reload: 'system-notice' });
                }, function() {
                    $state.go('system-notice');
                });
            }]
        })
        .state('system-notice.edit', {
            parent: 'system-notice',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-notice/system-notice-dialog.html',
                    controller: 'SystemNoticeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SystemNotice', function(SystemNotice) {
                            return SystemNotice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-notice', null, { reload: 'system-notice' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('system-notice.delete', {
            parent: 'system-notice',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/system-notice/system-notice-delete-dialog.html',
                    controller: 'SystemNoticeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SystemNotice', function(SystemNotice) {
                            return SystemNotice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('system-notice', null, { reload: 'system-notice' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
