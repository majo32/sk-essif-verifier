import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EssifVerificationComponent } from './essif-verification.component';
import { RouterModule } from '@angular/router';

const routes = [
  {
      path: '**',
      component: EssifVerificationComponent
  }
];


@NgModule({
  declarations: [EssifVerificationComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
  ]
})
export class EssifVerificationModule { }
