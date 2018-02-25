import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import {UserService} from "../_services";

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private user: UserService) {
    }

    canActivate() {
        return this.user.isLoggedIn();
    }
}