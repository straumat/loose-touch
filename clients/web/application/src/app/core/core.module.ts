import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CoreComponent} from './core.component';
import {HeaderComponent} from './header/header.component';
import {AppRoutingModule} from '../app-routing.module';
import {CoreRoutingModule} from './core-routing.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CustomMaterialModule} from "./material.module";

@NgModule({
  declarations: [CoreComponent, HeaderComponent],
  imports: [
    AppRoutingModule,
    CoreRoutingModule,
    CommonModule,
    NgbModule,
    CustomMaterialModule
  ]
})
export class CoreModule {
}
