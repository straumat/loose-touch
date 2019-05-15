import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from '../features/login/login.component';
import {CoreComponent} from './core.component';
import {DashboardComponent} from '../features/dashboard/dashboard.component';

export const routes: Routes = [
  {
    // Login.
    path: 'login',
    component: LoginComponent
  },
  {
    // Dashboard.
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full',
    // canActivate: [AuthenticationGuardService]
  },
  {
    path: '',
    component: CoreComponent,
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full',
        // canActivate: [AuthenticationGuardService]
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        // canActivate: [AuthenticationGuardService]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CoreRoutingModule {
}
