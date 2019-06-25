import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from '../features/login/login.component';
import {CoreComponent} from './core.component';
import {DashboardComponent} from '../features/dashboard/dashboard.component';
import {ErrorComponent} from '../features/error/error.component';
import {AuthenticationGuard} from './guards/authentication.guard';
import {LooseTouchError} from 'angular-loose-touch-api';
import {ContactComponent} from '../features/contact/contact.component';

// TODO : Remove export.
export const routes: Routes = [
  // Login.
  {
    path: 'login',
    component: LoginComponent
  },
  // Application.
  {
    path: '',
    component: CoreComponent,
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthenticationGuard]
      },
      {
        path: 'contacts/new',
        component: ContactComponent,
        canActivate: [AuthenticationGuard]
      }
    ]
  },
  // Errors 500, 401 & 404.
  {path: 'error', component: ErrorComponent},
  {
    path: '**', component: ErrorComponent, data: {
      looseTouchError: {
        type: LooseTouchError.TypeEnum.InvalidRequestError,
        message: 'Page not found',
        errors: []
      }
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule {
}
