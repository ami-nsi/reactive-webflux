import { NgModule } from '@angular/core';
import { SharedModule } from '@app/shared/shared.module';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';

const MODULES: any[] = [SharedModule, HomeRoutingModule];

const DECLARATIONS: any[] = [HomeComponent];

@NgModule({
  imports: MODULES,
  declarations: DECLARATIONS,
})
export class HomeModule {}
