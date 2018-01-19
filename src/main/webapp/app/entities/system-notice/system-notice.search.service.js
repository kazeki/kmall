(function() {
    'use strict';

    angular
        .module('kmallApp')
        .factory('SystemNoticeSearch', SystemNoticeSearch);

    SystemNoticeSearch.$inject = ['$resource'];

    function SystemNoticeSearch($resource) {
        var resourceUrl =  'api/_search/system-notices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
