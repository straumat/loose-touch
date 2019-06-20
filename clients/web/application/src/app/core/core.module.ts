import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CoreRoutingModule} from './core-routing.module';
import {CoreComponent} from './core.component';
import {CustomMaterialModule} from './material.module';
import {HeaderComponent} from './components/header/header.component';

@NgModule({
  declarations: [CoreComponent, HeaderComponent],
  imports: [
    CommonModule,
    CoreRoutingModule,
    CustomMaterialModule
  ]
})
export class CoreModule {
}
