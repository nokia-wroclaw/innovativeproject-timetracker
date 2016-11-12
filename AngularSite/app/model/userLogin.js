(function(app) {
	app.UserLogin = UserLogin;
	function UserLogin(login, email, pwd) {
		this.login = login;
		this.email = email;
		this.pwd = pwd;
	}
})(window.app || (window.app = {}));