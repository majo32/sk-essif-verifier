import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';
import { SessionService } from '../session.service';
import { VerifierESSIFService } from 'src/openapi/server/verifier';

@Component({
  selector: 'app-essif-verification',
  templateUrl: './essif-verification.component.html',
  styleUrls: ['./essif-verification.component.scss']
})
export class EssifVerificationComponent implements OnInit {

  step: number = 0;
  qr;
  json;
  holderUrl;
  credentialSubject;
  sessionId;

  constructor(private http: HttpClient,
    private activatedRoute: ActivatedRoute,
     public sessionService: SessionService,
     public apiService: VerifierESSIFService) { }

  ngOnInit(): void {

  }

  showQR(): void{
    this.step = 1;
    this.apiService.getVerifierGenerateQr().subscribe((resp)=>{
      this.qr = resp.qr;
      this.json = resp.request;
      this.holderUrl = resp.holderUrl;
      this.sessionId = resp.sessionId;
      this.checkState();
    })
  }

  checkState(): void{
    const interval = setInterval(()=>{

      this.apiService.postVerifierState({
        id: this.sessionId
      }).subscribe((resp)=>{
        const i = resp.state;
        if (i === 1){
          clearInterval(interval);
          this.apiService.postVerifierGetCredentials({
            id: this.sessionId
          }).subscribe((resp)=>{
            this.step = this.step + 1;
            this.credentialSubject = resp;
          })
        }
      });
    }, 1000);
  }
}
