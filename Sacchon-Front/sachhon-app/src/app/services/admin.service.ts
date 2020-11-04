import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IPatient } from '../models/patient';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  patientUrl = "http://localhost:9002/v1/patient?username="


  constructor(private http: HttpClient) { }

  public getPatient(pUsername: string, userUsername, password): Observable<any> {


    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(userUsername + ":" + password),
        }
      )
    }


    const usernameUrl = this.patientUrl + pUsername;

    return this.http.get(usernameUrl, httpOptions);

  }
}
