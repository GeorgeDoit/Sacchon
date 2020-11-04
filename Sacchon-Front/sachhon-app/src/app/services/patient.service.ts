import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IPatientMeasurment } from '../models/patientMeasurment';

import { IConsult } from '../models/consult';
import { NgIf } from '@angular/common';


@Injectable({
  providedIn: 'root'
})
export class PatientService {


  masurmentsUrl: string = "http://localhost:9002/v1/measurements?pid="
  avgStatsUrl: string = "http://localhost:9002/v1/stats/patient/avrg?pid="
  deleteUrl: string = "http://localhost:9002/v1/measurements/"
  measurmentsByDate: string = 'http://localhost:9002/v1/measurements?pid='
  consults: string = "http://localhost:9002/v1/consults?pid="
  submissions: string = "http://localhost:9002/v1/stats/patient/subs?pid="
  stateFree: string = "http://localhost:9002/v1/patient?state=free";
  availableForConsult: string = "http://localhost:9002/v1/patient?state=availableConsulted";
  patientsNoActivity: string = "http://localhost:9002/v1/patient?state=inactive&from=";
  patientByDoctor: string = "http://localhost:9002/v1/patient?did=";
  getpatient: string = "http://localhost:9002/v1/patient/";

  httpOptions = {
    headers: new HttpHeaders(
      {
        'Content-Type': 'application/json',
        'Authorization': `Basic ` + btoa("pat:pat"),
      }
    )
  };

  adminHttpOptions = {
    headers: new HttpHeaders(
      {
        'Content-Type': 'application/json',
        'Authorization': `Basic ` + btoa("admin:admin"),
      }
    )
  };
  constructor(private http: HttpClient) { }

  public addMeasurment(measurment, id: number, username, password): Observable<IPatientMeasurment> {
    let url = this.masurmentsUrl + id;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.post<IPatientMeasurment>(url, measurment, httpOptions);
  }

  public getMeasurments(id: number, username, password): Observable<IPatientMeasurment> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get<IPatientMeasurment>(this.masurmentsUrl + id, httpOptions);
  }

  public getMeasurmentsByDate(id: number, from: number, to: number, username, password): Observable<IPatientMeasurment> {

    const url = this.measurmentsByDate + id + '&from=' + from + '&to=' + to;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get<IPatientMeasurment>(url, httpOptions);
  }


  public getSubmissions(id: number, username, password): Observable<any> {
    const url = this.submissions + id;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(url, httpOptions);
  }

  public getSubmissions2(id: number, from: number, to: number, username, password): Observable<any> {
    const url = this.submissions + id + '&from=' + from + '&to=' + to;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(url, httpOptions);
  }

  public getConsults(id: number, username, password): Observable<IConsult> {

    const url = this.consults + id;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get<IConsult>(url, httpOptions);
  }

  public updateMeasurment(measurment, measurmentId: number, username, password): Observable<any> {
    let url = this.deleteUrl + measurmentId;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.put(url, measurment, httpOptions);
  }

  public deleteMeasurment(measurmentId: number, username, password): Observable<IPatientMeasurment> {
    let url = this.deleteUrl + measurmentId;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.delete<IPatientMeasurment>(url, httpOptions);
  }

  public getAvgStats(userId: number, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(this.avgStatsUrl + userId, httpOptions);
  }

  public getStateFreePat(username, password) {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(this.stateFree, httpOptions)
  }

  public getAvailableForConsult(username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(this.availableForConsult, httpOptions);
  }

  public getPatientsNoActive(from: number, to: number, username, password): Observable<any> {

    const url = this.patientsNoActivity + from + '&to=' + to;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.get(url, httpOptions);
  }

  public getPatientByDoctor(id: number, username, password): Observable<any> {

    const url = this.patientByDoctor + id;
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    return this.http.get(url, httpOptions);
  }


  updateAccount(id, patient, username, password): Observable<any> {

    const url = "http://localhost:9002/v1/patient/" + id;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.put(url, patient, httpOptions)
  }

  warnPat(id, username, password) {

    const url = "http://localhost:9002/v1/patient/" + id;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }


    return this.http.get(url, httpOptions);
  }

}


