export class User {
    constructor(
        public login: string,
        public pwd: string,
        public pwdR?: string,
        public name?: string,
        public surname?: string,
        public email?: string
    ) {}
}