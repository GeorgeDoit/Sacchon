import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AdminService } from 'src/app/services/admin.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-p-subs',
  templateUrl: './p-subs.component.html',
  styleUrls: ['./p-subs.component.scss']
})
export class PSubsComponent implements OnInit {


  subHoveredDate: NgbDate;
  subFromDate: NgbDate;
  subToDate: NgbDate;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;

  newSubFromDate: string;
  newSubToDate: string;
  searchPatientForm: FormGroup;
  patientUsername: any;

  patient: any = [];
  patient_id: number;
  patientSubs: [];
  patientResult: any = [];

  hidePatientInfo = true;
  username: any;
  password: any;

  constructor(
    private _location: Location,

    private adminService: AdminService,
    private userService: UserService,
    private patientService: PatientService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.searchPatientForm = new FormGroup({
      username: new FormControl('', Validators.required)
    });
    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();

  }

  searchPatientFormSubmitted() {
    let patArray = [];

    this.patientUsername = this.searchPatientForm.get('username').value;
    this.adminService.getPatient(this.patientUsername, this.username, this.password).subscribe(result => {
      patArray = result;

      if (patArray.length !== 0) {
        patArray.forEach(patient => {
          this.patient_id = patient.id;
          this.patientService.getSubmissions(patient.id, this.username, this.password).subscribe(response => {
            this.patientResult.push(response);
            this.toastr.success("User found, now select date range!", "Success!");
          })

        });
      } else {
        this.toastr.error("Patient does not exist", "Error!");
      }
    });

  }

  //1. The information submissions(personal monitor data) of a patient over a time range
  getSingePatientInfo() {
    this.adminService.getPatient(this.searchPatientForm.get('username').value, this.username, this.password).subscribe(result => {
      this.patient = result;

      this.patient.forEach(element => {
        this.patient_id = element.id;

        if (this.patient_id !== undefined) {

          return this.patient_id;

        } else {
          this.hidePatientInfo = true;
        }
      });
    });
  }

  closeSinglePatientInfo() {
    this.hidePatientInfo = true;
  }

  getSubsByDate() {
    if (this.subFromDate != null && this.subToDate != null) {

      const newFrom = new Date(this.newSubFromDate).getTime();
      const newTo = new Date(this.newSubToDate).getTime();


      if (this.patient_id !== undefined) {
        this.patientService.getSubmissions2(this.patient_id, newFrom, newTo, this.username, this.password).subscribe(response => {
          this.patientResult.push(response);
          this.patientResult.forEach(element => {
            this.patientSubs = element.subs;
            this.hidePatientInfo = false;
          });
        });
      }
    }
  }

  patientSubsDateSelection(date: NgbDate) {
    if (!this.subFromDate && !this.subToDate) {
      this.subFromDate = date;
    } else if (this.subFromDate && !this.subToDate && date.after(this.subFromDate)) {
      this.subToDate = date;
    } else {
      this.subToDate = null;
      this.subFromDate = date;
    }

    if (this.subFromDate != null && this.subToDate != null) {
      this.newSubFromDate = this.subFromDate.year + '-' + this.subFromDate.month + '-' + this.subFromDate.day;

      this.newSubToDate = this.subToDate.year + '-' + this.subToDate.month + '-' + this.subToDate.day;
    }
  }

  patientSubsIsHovered(date: NgbDate) {
    return this.subFromDate && !this.subToDate && this.subHoveredDate && date.after(this.subToDate) && date.before(this.subHoveredDate);

  }

  patientSubsIsInside(date: NgbDate) {
    return this.subToDate && date.after(this.subFromDate) && date.before(this.subToDate);

  }

  patientSubsIsRange(date: NgbDate) {
    return date.equals(this.subFromDate) || (this.subToDate && date.equals(this.subToDate)) || this.patientSubsIsInside(date) || this.patientSubsIsHovered(date);

  }

  backClicked() {
    this._location.back();
  }

  cLoseTable() {
    this.hidePatientInfo = true;
  }
}
