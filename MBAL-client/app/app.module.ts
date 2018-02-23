import { NgModule }      from '../node_modules/@angular/core';
import { BrowserModule } from '../node_modules/@angular/platform-browser';
import { FormsModule }    from '../node_modules/@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '../node_modules/@angular/common/http';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { AlertComponent } from './_directives';
import { AuthGuard } from './_guards';
import { JwtInterceptorProvider, ErrorInterceptorProvider } from './_helpers';
import { AlertService, AuthenticationService, UserService } from './_services';
import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';

import { JwtModule } from '@auth0/angular-jwt';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        routing,
        JwtModule.forRoot({
            config: {
                tokenGetter: () => {
                    return localStorage.getItem('auth_token');
                },
                whitelistedDomains: ['localhost:9000']
            }
        }),
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        HomeComponent,
        LoginComponent,
        RegisterComponent
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
        JwtInterceptorProvider,
        ErrorInterceptorProvider
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }