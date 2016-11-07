(function(app) {
  app.AppModule =
    ng.core.NgModule({
      imports: [ 
		ng.platformBrowser.BrowserModule,
		ng.forms.FormsModule,
	  ],
      declarations: [ 
		app.AppComponent,
		app.RegisterFormComponent,
		app.LoginRegisterComponent,
		app.LoginFormComponent
	  ],
      bootstrap: [ app.AppComponent ]
    })
    .Class({
      constructor: function() {}
    });
})(window.app || (window.app = {}));