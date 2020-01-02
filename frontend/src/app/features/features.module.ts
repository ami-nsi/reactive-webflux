import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

export const FEATURES_ROUTES: Routes = [
  { path: 'home', loadChildren: () => import('@app/features/home/home.module').then(m => m.HomeModule) },
];

const MODULES: any[] = [];

@NgModule({
  imports: [...MODULES, RouterModule.forChild(FEATURES_ROUTES)],
  exports: MODULES,
})
export class FeaturesModule {}
