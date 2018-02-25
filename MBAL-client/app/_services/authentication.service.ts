﻿import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

import { appConfig } from '../app.config';

@Injectable()
export class AuthenticationService {
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.post<any>(appConfig.apiUrlMethods + '/users/authenticate', { grant_type: "password", username: "admin", password: "Valentin34" })
            .map(user => {
                // login successful if there's a jwt token in the response
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return user;
            });
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }

    getAuthToken() {
        let body = 'grant_type=password&username=admin&password=Valentin34';
        let headersToSend = new HttpHeaders();
        headersToSend.append('Content-Type', 'application/x-www-form-urlencoded');
        headersToSend.append('Authorization','Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0');

        return this.http.post(appConfig.apiUrlToken, body,
            {headers:headersToSend})
            .map((data:Response) => data.json());
    }
}