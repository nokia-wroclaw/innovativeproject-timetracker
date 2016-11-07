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
	  getUsers: function() {
		  	var xhttp = new XMLHttpRequest();
			xhttp.open("GET", "http://localhost:9000/users", true);
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					document.getElementById("demo").innerHTML = xhttp.responseText;
				}
			};
			xhttp.send(); 
	  },
	  sendUser: function() {
			var xhttp = new XMLHttpRequest();
			var params = "TUTAJ TRESC";
			xhttp.open("POST", "http://localhost:9000/user", true);
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					document.getElementById("demo").innerHTML = xhttp.responseText;
				}
			};
			xhttp.send(); 
	  }
    });
})(window.app || (window.app = {}));