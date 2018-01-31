(function() {
    'use strict';
    angular
        .module('kmallApp')
        .factory('Goods', Goods);

    Goods.$inject = ['$resource'];

    function Goods ($resource) {
        var resourceUrl =  'api/goods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
