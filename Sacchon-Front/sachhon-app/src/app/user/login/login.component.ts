import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { first } from 'rxjs/operators';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  userId: number;
  users = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3)]),
      password: new FormControl('', [Validators.required, Validators.minLength(3)])
    });

  }

  public formSubmitted() {

    if (this.loginForm.valid) {
      this.userService.getUser(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(response => {
        this.users.push(response);
        this.userService.setUserName(this.loginForm.get('username').value);
        this.userService.setPassword(this.loginForm.get('password').value);

        this.checkUsersPath(this.users);
      }, error => {
        this.toast();
      });
    }
  }
  public toast() {
    this.toastr.error("Error!", "User's credentials are wrong")
  }

  public checkUsersPath(users: any[]) {

    let path: string = '';

    for (let user of users) {

      if (user.role === 'patient') {
        this.userId = user.patientId;
        path = user.role + '/' + this.userId;
      } else if (user.role === 'admin') {
        this.userId = user.adminId;
        path = user.role + '/' + this.userId;
      } else {
        this.userId = user.doctorId;
        path = user.role + '/' + this.userId;
      }

    }

    this.router.navigate([path]);
  }


}
