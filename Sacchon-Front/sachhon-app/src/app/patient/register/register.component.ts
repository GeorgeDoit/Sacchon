import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisterService } from 'src/app/services/register.service';
import { IPatient } from 'src/app/models/patient';
import { UserService } from 'src/app/services/user.service';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'sachhon-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})




export class RegisterComponent implements OnInit {
  userForm: FormGroup;
  users = [];

  constructor(
    private registerService: RegisterService,
    private toastr: ToastrService

  ) { }

  ngOnInit() {
    this.userForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(3)]),
      surname: new FormControl('', [Validators.required, Validators.minLength(3)]),
      dob: new FormControl('', [Validators.required]),
      amka: new FormControl('', [Validators.required, Validators.minLength(3), Validators.pattern("^[0-9]*$")]),
      gender: new FormControl('', [Validators.required, Validators.minLength(3)]),
      username: new FormControl('', [Validators.required, Validators.minLength(3)]),
      password: new FormControl('', [Validators.required, Validators.minLength(3)]),
    });
  }



  public forSubmited() {
    if (this.userForm.valid) {
      const dateOB = new Date(this.userForm.value.dob).getTime();

      let patient = {
        name: this.userForm.value.name,
        surname: this.userForm.value.surname,
        dob: dateOB,
        amka: this.userForm.value.amka,
        gender: this.userForm.value.gender,
        availableConsulted: false,
        username: this.userForm.value.username,
        password: this.userForm.value.password,
        dateMonthStarted: null,
        dateLastConsulted: null
      };


      try {
        throw new Error('En error happened');
      }
      catch (error) {
        console.error('Log error', error);
      }
      this.registerService.createUser(patient).subscribe(response => {
        this.toastr.success("Succees", "Account Created.. Login to continues",);
      }, error => {
        this.toastr.error("Error!", "Username taken");
      });

    }
  }

}

