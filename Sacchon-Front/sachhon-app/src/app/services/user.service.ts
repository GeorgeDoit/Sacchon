import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { url } from 'inspector';
import { ToastrService } from 'ngx-toastr';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { IUser } from '../models/user';
// import { IUser } from '../models/userInterface';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl: string = "http://localhost:9002/v1/user?username=";
  userRole: string;
  username: string;
  password: string;

  httpOptions: { headers: HttpHeaders; };
  constructor(private http: HttpClient, private toatr: ToastrService) { }

  public getUser(username, password): Observable<any> {

    let httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ':' + password),
        }
      )
    };

    let url = this.userUrl + username + '&password=' + password;

    return this.http.get<IUser[]>(url, httpOptions).pipe(
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



  setUserRole(role: string) {
    this.userRole = role;
  }

  getUserRole(): string {
    return this.userRole;
  }

  setUserName(username: string) {
    this.username = username;
  }

  getUserName(): string {
    return this.username;
  }

  setPassword(password: string) {
    this.password = password;
  }

  getPassword(): string {
    return this.password;
  }

  deleteUser(user: string, username, password): Observable<any> {

    let httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ':' + password),
        }
      )
    };

    const url = "http://localhost:9002/v1/" + user;
    return this.http.delete(url, httpOptions);
  }

  getUserData(role: string, id: number, username, password): Observable<any> {

    let httpOptions = {
      headers: new HttpHeaders(
        {
          'Content-Type': 'application/json',
          'Authorization': `Basic ` + btoa(username + ':' + password),
        }
      )
    };



    const url = 'http://localhost:9002/v1/' + role + '/' + id;
    return this.http.get(url, httpOptions);
  }
}



