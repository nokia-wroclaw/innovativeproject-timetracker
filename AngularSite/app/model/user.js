(function(app) {
	app.User = User;
	function User(name, surname, login, email, pwd) {
        this.name = name;
		this.surname = surname;
		this.login = login;
		this.email = email;
		this.pwd = pwd;

	}
})(window.app || (window.app = {}));