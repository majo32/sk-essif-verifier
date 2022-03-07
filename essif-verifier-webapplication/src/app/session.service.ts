import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  token = '';

  constructor() { }

  setToken(token): void{
    this.token = token;
  }

  getToken(): string{
    return this.token;
  }

  existsSession(): boolean{
    return !!this.token;
  }
}
