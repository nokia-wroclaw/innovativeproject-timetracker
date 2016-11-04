mocha 		= ./node_modules/.bin/mocha
karma 		= ./node_modules/karma/bin/karma
jshint		= ./node_modules/.bin/jshint
linelint 	= ./node_modules/.bin/linelint
browserify 	= ./node_modules/.bin/browserify
lintspaces 	= ./node_modules/.bin/lintspaces

srcFiles = $(shell find ./lib -type f -name '*.js' | xargs)

.PHONY : test

default: format

build:format
	$(browserify) -e ./lib/index.js -o ./dist/ng-local-storage.js
	@echo "Build succeeded!\n"

test:build
	$(karma) start

format:
	$(linelint) $(srcFiles) $(testFiles)
	@echo "linelint pass!\n"
	$(lintspaces) -nt -i js-comments -d spaces -s 2 $(srcFiles)
	@echo "lintspaces pass!\n"
	$(jshint) $(srcFiles)
	@echo "JSHint pass!\n"