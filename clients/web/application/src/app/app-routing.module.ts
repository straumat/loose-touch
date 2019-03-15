import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {FullLayoutComponent} from "./layouts/full/full-layout.component";

import {Full_ROUTES} from "./shared/routes/full-layout.routes";

import {AuthGuard} from './shared/auth/auth-guard.service';

const appRoutes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: '',
    component: FullLayoutComponent,
    data: {title: 'full Views'},
    children: Full_ROUTES,
    canActivate: [AuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})

export class AppRoutingModule {

}
