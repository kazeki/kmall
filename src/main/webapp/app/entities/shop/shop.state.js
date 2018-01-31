(function() {
    'use strict';

    angular
        .module('kmallApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shop', {
            parent: 'entity',
            url: '/shop?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.shop.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shop/shops.html',
                    controller: 'ShopController',
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
                    $translatePartialLoader.addPart('shop');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shop-detail', {
            parent: 'shop',
            url: '/shop/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kmallApp.shop.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shop/shop-detail.html',
                    controller: 'ShopDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shop');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Shop', function($stateParams, Shop) {
                    return Shop.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'shop',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('shop-detail.edit', {
            parent: 'shop-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shop/shop-dialog.html',
                    controller: 'ShopDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shop', function(Shop) {
                            return Shop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shop.new', {
            parent: 'shop',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shop/shop-dialog.html',
                    controller: 'ShopDialogController',
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
                    $state.go('shop', null, { reload: 'shop' });
                }, function() {
                    $state.go('shop');
                });
            }]
        })
        .state('shop.edit', {
            parent: 'shop',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shop/shop-dialog.html',
                    controller: 'ShopDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Shop', function(Shop) {
                            return Shop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shop', null, { reload: 'shop' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shop.delete', {
            parent: 'shop',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shop/shop-delete-dialog.html',
                    controller: 'ShopDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Shop', function(Shop) {
                            return Shop.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shop', null, { reload: 'shop' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
