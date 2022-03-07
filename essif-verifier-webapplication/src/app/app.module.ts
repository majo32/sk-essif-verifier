import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { EssifVerificationModule } from './essif-verification/essif-verification.module';
import { HttpClientModule } from '@angular/common/http';
import { ESSIFVerifierApiModule, ESSIFVerifierConfiguration } from 'src/openapi/server/verifier';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    EssifVerificationModule,
    HttpClientModule,

    ESSIFVerifierApiModule.forRoot(()=>{
      return new ESSIFVerifierConfiguration({
        basePath: "/api/"
      });
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
