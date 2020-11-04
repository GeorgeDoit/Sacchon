import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUser } from '../models/userInterface';


@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private userUrl: string = "http://localhost:9002/v1/users";

  constructor(private http: HttpClient) { }

  public getUsers(username, password): Observable<IUser[]> {

    const authorizationData = 'Basic ' + btoa(username + ':' + password);

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'TOKEN'
      })
    };

    return this.http.get<IUser[]>(this.userUrl, httpOptions);
  }



  public createUser(user: IUser): Observable<any> {
    return this.http.post(this.userUrl, user);
  }

  // public deleteUser(id: number){
  //   return this.http.delete(this.userUrl, '/1' );
  // }

}



