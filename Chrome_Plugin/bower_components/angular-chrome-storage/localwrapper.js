(function(window, angular, undefined) {
    'use strict';

    var Codec = function() {
        this.JSONMarker = "___JSON:";
        this.encode = function(val) {
            if (angular.isObject(val) || angular.isArray(val)) {
                val = this.JSONMarker + angular.toJson(val);
            } else if (typeof val == "undefined") {
                val = null;
            }
            return val;
        };

        this.decode = function(val) {
            if (typeof val === 'undefined') {
                return undefined;
            }
            if (val.slice(0, this.JSONMarker.length) === this.JSONMarker) {
                val = angular.fromJson(val.slice(this.JSONMarker.length));
            }
            return val;
        };

        return this;
    };

    var LocalStrategy = function() {
        var self = this;
        this.get = function(key, cb) {
            cb(this.Codec.decode(localStorage[key]));
        };


        this.set = function(key, val, cb) {
            localStorage[key] = this.Codec.encode(val);
            if (!!cb) {
                cb();
            }
        };

        this.remove = function(key, cb) {
            delete localStorage[key];
            if (!!cb) {
                cb();
            }
        };

        this.Codec = new Codec();
    };

    var ChromeStoreStrategy = function() {
        var chromeStore;
        this.setSync = function(useSync) {
            if (typeof chrome === "undefined" || typeof chrome.storage === "undefined") {
                throw new Error("Can't use ChromeStoreStrategy; chrome.storage is undefined.");
            }
            if (useSync) {
                // Sync was requested, but is it available?
                if (chrome.storage.sync) {
                    chromeStore = chrome.storage.sync;
                } else {
                    throw new Error("Can't use sync; chrome.storage.sync is not available");
                }
            } else {
                chromeStore = chrome.storage.local;
            }
        };


        this.set = function(key, val, cb) {
            // chrome.storage requires an object be passed in to the set function
            var newSomething = {};
            newSomething[key] = val;
            chromeStore.set(newSomething, function() {
                if (!!cb) {
                    cb();
                }
            });
        };

        this.get = function(key, cb) {
            chromeStore.get(key, function(data) {
                // chrome.storage returns an Object containing { key : value }
                // need to make sure to pull that out when returning
                cb(data[key]);
            });
        };

        this.remove = function(key, cb) {
            chromeStore.remove(key, function() {
                if (!!cb) {
                    cb();
                }
            });
        };
    };

    angular.module('angular-chrome-storage', [])
        .service('LocalWrapper', function() {

            var strategy = null;

            this.setSync = function(useSync) {
                if (typeof strategy !== "ChromeStoreStrategy") {
                    throw new Error("Can't use sync without Chrome Storage");
                } else {
                    ChromeStoreStrategy.setSync(useSync);
                }
            };

            this.setStrategy = function(s) {
                strategy = s;
            };

            this.set = function(key, val, cb) {
                return strategy.set(key, val, cb);
            };

            this.get = function(key, cb) {
                return strategy.get(key, cb);
            };

            this.remove = function(key, cb) {
                return strategy.remove(key, cb);
            };

            // Initialize to non-sync local storage, whichever type is available.
            // Sync has to be explicitly enabled via setSync() if desired.
            if (typeof chrome !== "undefined" && typeof chrome.storage !== "undefined" && !!chrome.storage) {
                ChromeStoreStrategy.setSync(false);
                this.setStrategy(new ChromeStoreStrategy());
            } else {
                this.setStrategy(new LocalStrategy());
            }
        });
})(window, window.angular);
