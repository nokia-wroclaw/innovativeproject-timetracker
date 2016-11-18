import { Injectable } from "@angular/core";

@Injectable()
export class UserService {
    private loggedIn = false;

    constructor() {
        //TODO: Tutaj czy istnieje token, jesli tak to loggedIn = true
    }

    loginRequest(login: string, password: string) {
        var xhttp = new XMLHttpRequest();
        var params = JSON.stringify({
            login: login,
            password: password
        });

        xhttp.open("POST", "http://localhost:9000/login", true);
        xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        xhttp.onreadystatechange = function(){
            if (this.readyState == 4 && this.status == 200) {
                //TODO: Tutaj localstorage lub cookie?
                document.getElementById("loginServerAnswer").innerHTML = xhttp.responseText;
            }
        };
        xhttp.send(params);
    }

    logoutRequest() {
        //TODO: Usuniecie ciasteczka lub localstorage?
    }

    isLoggedIn() {
        return this.loggedIn;
    }
}