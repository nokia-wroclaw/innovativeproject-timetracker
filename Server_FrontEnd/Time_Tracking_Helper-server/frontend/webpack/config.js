var path = require('path');
var util = require('util');
var autoprefixer = require('autoprefixer-core');
var pkg = require('../../package.json');

var loaders = require('./loaders');
var plugins = require('./plugins');

var jsBundle = util.format('[name].%s.js', pkg.version);

var entry = {
    login: ['./loginEntry.ts'],
    index: ['./indexEntry.ts'],
    register: ['./registerEntry.ts'],
    timeline: ['./timelineEntry.ts'],
    schedule: ['./scheduleEntry.ts'],
    privileges: ['./privilegesEntry.ts'],
    settings: ['./settingsEntry.ts']
};

var config = {
    context: path.join(__dirname, '../src'),
    debug: true,
    target: 'web',
    entry: entry,
    output: {
        path: path.resolve(pkg.config.buildDir),
        publicPath: '/assets/app/',
        filename: jsBundle,
        pathinfo: false
    },
    module: {
        loaders: loaders
    },
    postcss: [
        autoprefixer
    ],
    plugins: plugins,
    resolve: {
        extensions: ['', '.js', '.json', '.ts']
    },
    devServer: {
        contentBase: path.resolve(pkg.config.buildDir),
        hot: true,
        noInfo: false,
        inline: true,
        stats: {colors: true}
    },
    watchOptions: {
        aggregateTimeout: 0
    }
};

module.exports = config;
