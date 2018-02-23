import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import {AlertService, AuthenticationService, UserService} from '../_services/index';

@Component({
    moduleId: module.id,
    templateUrl: 'login.component.html'
})

export class LoginComponent {
    model: any = {};
    loading = false;
    returnUrl: string;

    constructor(private userService: UserService, private router: Router) {}

    onSubmit(email:string, password:string) {
        this.userService.login(email, password).subscribe((result) => {
            if (result) {
                this.router.navigate(['']);
            }
        });
    }
}
