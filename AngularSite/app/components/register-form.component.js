(function(app) {
  app.RegisterFormComponent = ng.core
    .Component({
      selector: 'register-form',
      templateUrl: 'app/components/register-form.component.html'
    })
    .Class({
      constructor: [function() {
        this.model = new app.User('', '', '', '', '');
        this.submitted = false;
      }],
      onSubmit: function() {
        this.submitted = true;
      },
      // TODO: Remove this when we're done
      diagnostic: function() {
        return JSON.stringify(this.model);
      },
    });
})(window.app || (window.app = {}));