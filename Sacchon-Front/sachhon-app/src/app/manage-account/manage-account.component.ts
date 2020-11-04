import { Location } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { IPatient } from '../models/patient';
import { DoctorService } from '../services/doctor.service';
import { PatientService } from '../services/patient.service';
import { UserService } from '../services/user.service';
import { DatePipe } from '@angular/common'

@Component({
  selector: 'sachhon-manage-account',
  templateUrl: './manage-account.component.html',
  styleUrls: ['./manage-account.component.scss']
})
export class ManageAccountComponent implements OnInit {
  private routeSub: Subscription;
  userId: number;
  patientForm: FormGroup;
  doctorForm: FormGroup;
  username: string;
  password: string;
  role: string;
  userdata: any = [];

  getDoc: boolean = false;
  getPat: boolean = false;

  user: any = [];

  constructor(
    private _location: Location,
    private toastr: ToastrService,
    public datepipe: DatePipe,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    private router: Router,
    private patientService: PatientService,
    private doctorService: DoctorService,
    private userService: UserService) { }

  backClicked() {
    this._location.back();
  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      this.userId = params['id'];
    });

    this.patientForm = new FormGroup({
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
      amka: new FormControl('', Validators.required),
      dob: new FormControl('', Validators.required),
      gender: new FormControl('', Validators.required)
    });

    this.doctorForm = new FormGroup({
      name: new FormControl('', Validators.required),
      surname: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),

    });

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();
    this.role = this.userService.getUserRole();

    if (this.role === 'patient') {
      this.getPat = true;
    } else {
      this.getDoc = true;
    }

  }

  closeResult = '';
  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'addModal' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
  openModal(targetModal, user) {


    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

    this.userService.getUserData(this.role, this.userId, this.username, this.username).subscribe(res => {
      this.userdata.push(res);

      this.userdata.forEach(element => {

        if (this.role === 'patient') {

          const dob = new Date(element.dob);
          let latest_date = this.datepipe.transform(dob, 'yyyy-MM-dd');

          this.patientForm.patchValue({
            name: element.name,
            surname: element.surname,
            amka: element.amka,
            dob: latest_date,
            gender: element.gender
          });

        } else {
          this.doctorForm.patchValue({
            name: element.name,
            surname: element.surname,
          });
        }

      });

    });

  }

  updateModalFormSubmit() {

    this.modalService.dismissAll();

    const res = this.patientForm.getRawValue();
    const patient = {
      name: res.name,
      surname: res.surname,
      amka: res.amka,
      dob: res.dob,
      gender: res.gender
    };

    this.patientService.updateAccount(this.userId, patient, this.username, this.password).subscribe(res => {
      this.toastr.success("Success!", "Account updated")
    }, error => {
      this.toastr.error("Error!", "Account update error")

    });
  }

  docModalFormSubmit() {

    this.modalService.dismissAll();

    const res = this.doctorForm.getRawValue();
    const doctor = {
      name: res.name,
      surname: res.surname,
    };

    this.doctorService.updateAccount(this.userId, doctor, this.username, this.password).subscribe(res => {
      this.toastr.success("Success!", "Account updated")
    }, error => {
      this.toastr.error("Error!", "Account update error")

    });
  }

  deleteAccount() {
    this.modalService.dismissAll();

    if (this.role !== undefined) {
      this.userService.deleteUser(this.role + '/' + this.userId, this.username, this.password).subscribe(res => {
        this.toastr.success("Account has been deleted! ", "Success");

        this.router.navigate(['login'])
      }, error => {

        this.toastr.error("Error", "Delete action failed",);
      });
    }
  }

  logout() {
    this.router.navigate(['login']);
  }

}
