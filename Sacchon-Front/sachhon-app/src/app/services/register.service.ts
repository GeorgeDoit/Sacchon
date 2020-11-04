import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { IPatient } from '../models/patient';
import { retry, catchError } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private userUrl: string = "http://localhost:9002/v1/signUp";


  constructor(private http: HttpClient, private toastr: ToastrService) { }

  createUser(patient): Observable<any> {

    return this.http.post(this.userUrl, patient).pipe(
      retry(1),
      catchError(this.handleError)
    );
  }

  handleError(error) {
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


}
