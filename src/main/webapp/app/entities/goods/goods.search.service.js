(function() {
    'use strict';

    angular
        .module('kmallApp')
        .factory('GoodsSearch', GoodsSearch);

    GoodsSearch.$inject = ['$resource'];

    function GoodsSearch($resource) {
        var resourceUrl =  'api/_search/goods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
