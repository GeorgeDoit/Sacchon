import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { DoctorService } from 'src/app/services/doctor.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-d-subs',
  templateUrl: './d-subs.component.html',
  styleUrls: ['./d-subs.component.scss']
})
export class DSubsComponent implements OnInit {
  hideDoctorConsults = true;
  hideWaitingP = true;
  hideInactiveP = true;
  hideInactiveD = true;
  validDoctorButton = 'disable';

  // date: NgbDateStruct = { year: 1789, month: 7, day: 14 };
  searchDoctorForm: FormGroup

  doctorUsername: any;
  doctorId: any;
  doctorSubs: any;
  doctorsConsults: any = [];

  docConsultHoveredDate: NgbDate;
  docConsultFromDate: NgbDate;
  docConsultToDate: NgbDate;
  newDocConsultFromDate: string;
  newDocConsultToDate: string;
  fromDate: NgbDate | null;
  toDate: NgbDate | null;
  username: string;
  password: string;


  constructor(private userService: UserService, private doctorService: DoctorService, private _location: Location, private toastr: ToastrService) { }

  ngOnInit() {
    this.searchDoctorForm = new FormGroup({
      username: new FormControl('', Validators.required)
    });

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();
  }

  backClicked() {
    this._location.back();
  }

  //2.The information submissions (consultations) of a doctor over a time range
  searchDoctorFormSubmitted() {

    let docArray = [];
    this.doctorUsername = this.searchDoctorForm.get('username').value;
    this.doctorService.getDoctorId(this.doctorUsername, this.username, this.password).subscribe(result => {
      docArray = result;

      if (docArray.length !== 0) {
        docArray.forEach(element => {
          this.toastr.success("Doctor found, now select date range! ", "Success!");

          this.doctorId = element.id;
        });
      } else {
        this.toastr.error("Doctor not found ", "Error!");
      }
    });
  }

  closeDoctorsInfo() {
    this.hideDoctorConsults = true;
  }

  getConsultsByDate() {
    if (this.docConsultFromDate != null && this.docConsultToDate != null) {

      const newFrom = new Date(this.newDocConsultFromDate).getTime();
      const newTo = new Date(this.newDocConsultToDate).getTime();

      if (this.doctorId !== undefined) {
        this.doctorService.getConsults(this.doctorId, newFrom, newTo, this.username, this.password).subscribe(response => {
          this.doctorsConsults.push(response);
          this.doctorsConsults.forEach(element => {
            this.doctorSubs = element.subs;

            this.hideDoctorConsults = false;
          });
        });
      }

    }
  }

  doctorConsultsDateSelection(date: NgbDate) {
    if (!this.docConsultFromDate && !this.docConsultToDate) {
      this.docConsultFromDate = date;
    } else if (this.docConsultFromDate && !this.docConsultToDate && date.after(this.docConsultFromDate)) {
      this.docConsultToDate = date;
    } else {
      this.docConsultToDate = null;
      this.docConsultFromDate = date;
    }

    if (this.docConsultFromDate != null && this.docConsultToDate != null) {
      this.newDocConsultFromDate = this.docConsultFromDate.year + '-' + this.docConsultFromDate.month + '-' + this.docConsultFromDate.day;

      this.newDocConsultToDate = this.docConsultToDate.year + '-' + this.docConsultToDate.month + '-' + this.docConsultToDate.day;
    }
  }

  docConsultIsHovered(date: NgbDate) {
    return this.docConsultFromDate && !this.docConsultToDate && this.docConsultHoveredDate && date.after(this.docConsultToDate) && date.before(this.docConsultHoveredDate);
  }

  docConsultIsInside(date: NgbDate) {
    return this.docConsultToDate && date.after(this.docConsultFromDate) && date.before(this.docConsultToDate);
  }

  docConsultIsRange(date: NgbDate) {
    return date.equals(this.docConsultFromDate) || (this.docConsultToDate && date.equals(this.docConsultToDate)) || this.docConsultIsInside(date) || this.docConsultIsHovered(date);
  }
}
