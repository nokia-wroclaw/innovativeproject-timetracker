(function(app) {
  app.LoginFormComponent = ng.core
    .Component({
      selector: 'login-form',
      templateUrl: 'app/components/login-form.component.html'
    })
    .Class({
      constructor: [function() {
        this.model = new app.UserLogin('', '', '');
        this.submitted = false;
      }],
      onSubmit: function() {
        this.submitted = true;
      },
    });
})(window.app || (window.app = {}));