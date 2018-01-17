(function() {
    'use strict';

    angular
        .module('kmallApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('goods', {
            parent: 'entity',
            url: '/goods',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.goods.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goods/goods.html',
                    controller: 'GoodsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goods');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('goods-detail', {
            parent: 'goods',
            url: '/goods/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.goods.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goods/goods-detail.html',
                    controller: 'GoodsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goods');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Goods', function($stateParams, Goods) {
                    return Goods.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'goods',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('goods-detail.edit', {
            parent: 'goods-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-dialog.html',
                    controller: 'GoodsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goods', function(Goods) {
                            return Goods.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goods.new', {
            parent: 'goods',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-dialog.html',
                    controller: 'GoodsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: 'goods' });
                }, function() {
                    $state.go('goods');
                });
            }]
        })
        .state('goods.edit', {
            parent: 'goods',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-dialog.html',
                    controller: 'GoodsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goods', function(Goods) {
                            return Goods.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: 'goods' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goods.delete', {
            parent: 'goods',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-delete-dialog.html',
                    controller: 'GoodsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Goods', function(Goods) {
                            return Goods.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: 'goods' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
