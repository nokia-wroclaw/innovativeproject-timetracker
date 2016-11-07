(function(app) {
  app.LoginRegisterComponent = ng.core
    .Component({
      selector: 'login-register',
      templateUrl: 'app/components/login-register.component.html'
    })
    .Class({
      constructor: [function() {
		this.choiceloginregister = false;
		this.login = true;
		this.register = true;
      }],
      onRegister: function() {
        this.register = false;
		this.login = true;
      },
	  onLogin: function() {
        this.login = false;
		this.register = true;
      },
      // TODO: Remove this when we're done
      diagnostic: function() {
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				document.getElementById("demo").innerHTML = xmlhttp.responseText;
			}
		};
		xhttp.open("GET", "http://localhost:9000/users", true);
		xhttp.send(); 
		
		//var xmlHttp = new XMLHttpRequest();
		//xmlHttp.open( "GET", "http://localhost:9000/users", false ); // false for synchronous request
		//xmlHttp.send(null);
		//return xmlHttp.responseText;
        return JSON.stringify(this.model);
      },
    });
})(window.app || (window.app = {}));