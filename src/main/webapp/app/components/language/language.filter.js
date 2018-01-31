(function() {
    'use strict';

    angular
        .module('kmallApp')
        .filter('findLanguageFromKey', findLanguageFromKey)
        .filter('findLanguageRtlFromKey', findLanguageRtlFromKey);

    var languages = {
        'zh-cn': { name: '中文（简体）' },
        'en': { name: 'English' },
        'ja': { name: '日本語' }
        // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    };

    function findLanguageFromKey() {
        return findLanguageFromKeyFilter;

        function findLanguageFromKeyFilter(lang) {
            return languages[lang].name;
        }
    }

    function findLanguageRtlFromKey() {
        return findLanguageRtlFromKeyFilter;

        function findLanguageRtlFromKeyFilter(lang) {
            return languages[lang].rtl;
        }
    }

})();
