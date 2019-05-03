import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {CoreComponent} from './core.component';
import {DashboardComponent} from '../features/dashboard/dashboard.component';
import {LoginComponent} from '../features/login/login.component';
import {PageNotFoundComponent} from '../features/page-not-found/page-not-found.component';
import {AuthenticationGuardService} from './guards/authentication-guard.service';
import {ProfileComponent} from '../features/profile/profile.component';

const coreRoutes: Routes = [
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
    canActivate: [AuthenticationGuardService]
  },
  {
    path: '',
    component: CoreComponent,
    children: [
      {
        path: '',
        component: DashboardComponent,
        canActivate: [AuthenticationGuardService]
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        // canActivate: [AuthenticationGuardService]
      },
      {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthenticationGuardService]
      }
    ]
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(coreRoutes)],
  exports: [RouterModule]
})
export class CoreRoutingModule {
}
