import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from '../features/login/login.component';
import {CoreComponent} from './core.component';
import {DashboardComponent} from '../features/dashboard/dashboard.component';
import {ErrorComponent} from '../features/error/error.component';
import {LooseTouchErrorType} from './models/looseToucheError';

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
        // canActivate: [AuthenticationGuardService]
      }
    ]
  },
  // Errors (500, 401 & 404).
  {path: 'error', component: ErrorComponent},
  {
    path: '**', component: ErrorComponent, data: {
      title: 'test',
      looseTouchError: {
        type: LooseTouchErrorType.invalid_request_error,
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
