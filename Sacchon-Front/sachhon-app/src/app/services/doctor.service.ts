import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { IConsult } from '../models/consult';
import { IDoctor } from '../models/doctor';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  url: string = "http://localhost:9002/v1/doctor";

  consultsUrl: string = "http://localhost:9002/v1/stats/consult?did=";
  inactiveUrl: string = "http://localhost:9002/v1/doctor?state=inactive&from=";

  postConsultUrl: string = "http://localhost:9002/v1/consults?did=";
  updateConsult: string = "http://localhost:9002/v1/consults/"

  constructor(private http: HttpClient, private toastr: ToastrService) { }

  public createDoctor(doctor: IDoctor, userUsername, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(userUsername + ":" + password),
        }
      )
    }

    return this.http.post(this.url, doctor, httpOptions)
  }

  docExist(error) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;

    }
    return throwError(errorMessage);
  }

  getConsults(id: number, fromDate: number, toDate: number, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = this.consultsUrl + id + '&from=' + fromDate + '&to=' + toDate;

    return this.http.get(url, httpOptions);
  }

  getConsults2(did: number, pid: number, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = "http://localhost:9002/v1/consults?did=" + did + '&pid=' + pid;

    return this.http.get(url, httpOptions);
  }

  getAllConsults(id: string, userUsername, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(userUsername + ":" + password),
        }
      )
    }


    return this.http.get(this.consultsUrl + id, httpOptions);
  }


  getDoctorId(username: string, userUsername, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(userUsername + ":" + password),
        }
      )
    }

    const url = "http://localhost:9002/v1/doctor?username=" + username;

    return this.http.get(url, httpOptions);
  }

  getInactive(from: number, to: number, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = this.inactiveUrl + from + '&to=' + to;
    return this.http.get(url, httpOptions);
  }

  postConsult(dId: string, pId: number, consult: any, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = this.postConsultUrl + dId + '&pid=' + pId;
    return this.http.post(url, consult, httpOptions).pipe(
      retry(1),
      catchError(this.handleError1)
    );

  }

  putConsult(cId: number, consult: any, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = this.updateConsult + cId;

    return this.http.put(url, consult, httpOptions)
  }



  handleError1(error, message) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;

    }
    return throwError(errorMessage);
  }



  handleError(error, message) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;

    }
    return throwError(errorMessage);
  }

  modifyConsult(consult: any, id: number, username, password): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }

    const url = "http://localhost:9002/v1/consults/" + id;

    return this.http.put(url, consult, httpOptions).pipe(
      retry(1),
      catchError(this.handleError)
    );

  }

  updateAccount(id, doctor, username, password): Observable<any> {

    const url = "http://localhost:9002/v1/doctor/" + id;

    const httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ":" + password),
        }
      )
    }
    return this.http.put(url, doctor, httpOptions);
  }
}
