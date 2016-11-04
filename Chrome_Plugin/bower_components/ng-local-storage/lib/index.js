'use strict';

module.exports = angular.module('ngLocalStorage', []).service('$localStorage',
  function($q, $timeout) {
    var ls = require('browser-ls');

    this.getAdapter = function (name) {
      return wrapAdapter(ls.getAdapter(name));
    };


    /**
     * A callback that will execute the correct promise function
     * based on the returned result.
     * @return {Function}
     */
    function promiseBack (promise) {
      return function (err, res) {
        if (err) {
          promise.reject(err);
        } else {
          promise.resolve(res);
        }
      };
    }


    /**
     * Wrap an adapter for friendly use with Angualr promises
     * @param  {[type]} adapter [description]
     * @return {[type]}         [description]
     */
    function wrapAdapter (adapter) {
      var fns = [
        'get',
        'set',
        'remove',
        'setJson',
        'getJson',
        'clear'
      ];

      // Loop over functions that require a wrapper and wrap em
      for (var i = 0; i < fns.length; i++) {
        var fn = fns[i];
        adapter[fn] = wrapFn(adapter, adapter[fn]);
      }

      return adapter;
    }


    /**
     * Wrap the provided function so that it's now Angular friendly.
     * @param  {Function} fn
     * @return {Function}
     */
    function wrapFn (ctx, fn) {
      return function () {
        // Original args provided by user as an Array
        var oArgs = Array.prototype.slice.call(arguments)
          , p = $q.defer();

        // Insert a callback function
        oArgs.push(promiseBack(p));

        $timeout(function () {
          fn.apply(ctx, oArgs);
        });

        return p.promise;
      };
    }
  });
