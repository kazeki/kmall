(function() {
    'use strict';
    angular
        .module('kmallApp')
        .factory('SystemNotice', SystemNotice);

    SystemNotice.$inject = ['$resource'];

    function SystemNotice ($resource) {
        var resourceUrl =  'api/system-notices/:id';

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
