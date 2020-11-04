import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { IDoctor } from '../models/doctor';
import { DoctorService } from '../services/doctor.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'sachhon-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  private routeSub: Subscription;
  adminId: number;

  doctor: IDoctor;
  createDoctorForm: FormGroup;
  username: any;
  password: any;

  constructor(

    private doctorService: DoctorService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.createDoctorForm = new FormGroup({
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    this.routeSub = this.route.params.subscribe(params => {
      this.adminId = params['id'];
    });

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();

  }

  createDoctorFormSubmitted() {

    this.doctor = {
      name: this.createDoctorForm.get('name').value,
      surname: this.createDoctorForm.get('name').value,
      username: this.createDoctorForm.get('name').value,
      password: this.createDoctorForm.get('name').value,
    }

    this.doctorService.createDoctor(this.doctor, this.username, this.password).subscribe(result => {
      this.toastr.success("Success!", "New Doctor account created")
    }, error => {
      this.toastr.error("Error!", "Doctor exists")

    });
  }

  goToPatientSubs() {
    this.router.navigate(['admin/patients-submissions/' + this.adminId]);
  }

  goToDoctorSubs() {
    this.router.navigate(['admin/doctors-submissions/' + this.adminId]);
  }

  goToPatientWaiting() {
    this.router.navigate(['admin/patients-waiting/' + this.adminId]);

  }

  goToPatientActivity() {
    this.router.navigate(['admin/patients-activity/' + this.adminId]);

  }

  goToDoctorActivity() {
    this.router.navigate(['admin/doctors-activity/' + this.adminId]);

  }

  logout() {
    this.router.navigate(['login']);
  }
}

