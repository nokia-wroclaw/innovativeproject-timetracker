# ng-local-storage
AngularJS friendly wrapper for the LocalStorage API.

## Install
Install with npm if using Browserify:

##### Using NPM
```
npm install ng-local-storage --save
```

##### Using a Manual Build
Alternatively clone this repo and run:

```
npm i
npm run build
```

Once finished the _dist_ folder will contain a file you can include in your 
project.


## Usage & Example
Simply add _ngLocalStorage_ as a dependency of your project.

```javascript
angular.module('myModule', ['ngLocalStorage'])
	.service('Tasks', function ($localStorage) {
		// The passed name "Tasks" is optional, but useful as it will namespace
		// the saved data to avoid conflicts
		var ls = $localStorage.getAdapter('Tasks');

		// Add task to window.localStorage
		this.addTask = function (name, description) {
			return ls.set(name, description);
		};
	});
```

## API
Internally this module uses 
[browser-ls](https://github.com/evanshortiss/browser-local-storage) 
so any function it exposes is exposed by this Angular service with two small 
differences.

### Difference #1
You must always use the _getAdapter_ function to get an instance as methods 
aren't bound directly to the service object.

### Difference #2
Callbacks aren't supported. Instead the library will return promises for all 
functions except _getAdapter_. So in browser-ls you might do this:

```javascript
function doSet(key, val, callback) {
	ls.set(key, val, callback);	
}
```

With ng-local-storage the above code becomes:


```javascript
function doSet(key, val) {
	// Return the promise!
	return ls.set(key, val);
}
```

### Functions

#### getAdapter (name)
This will get a localStorage interface that stores data under the given key 
name to avoid clashes.

All of the following methods return a promise.

#### get(key)
Get a string value from localStorage.

#### getJson(key)
Get an Object from localStorage.

#### set(key, string)
Set a string value in localStorage.

#### setJson(key, object)
Write a JSON object to localStorage.

#### remove(key)
Remove an object from localStorage.

## Contributing
Follow the existing style (there's a _.editorconfig_ so it's easy) and create a 
PR. Add tests if required.
