import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { AdminService } from 'src/app/services/admin.service';
import { DoctorService } from 'src/app/services/doctor.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-d-wo-activity',
  templateUrl: './d-wo-activity.component.html',
  styleUrls: ['./d-wo-activity.component.scss']
})
export class DWoActivityComponent implements OnInit {

  measurmentHoveredDate: NgbDate;
  measurmentFromDate: NgbDate;
  measurmentToDate: NgbDate;

  newMeasurmentFromDate: string;
  newMeasurmentToDate: string;
  doctors = [];
  hideDoctors: boolean = true;
  username: string;
  password: string;

  constructor(private _location: Location,
    private userService: UserService,
    private doctorService: DoctorService) { }

  ngOnInit() {
    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();
  }

  backClicked() {
    this._location.back();
  }


  onMeasurmentDateSelection(date: NgbDate) {
    if (!this.measurmentFromDate && !this.measurmentToDate) {
      this.measurmentFromDate = date;
    } else if (this.measurmentFromDate && !this.measurmentToDate && date.after(this.measurmentFromDate)) {
      this.measurmentToDate = date;
    } else {
      this.measurmentToDate = null;
      this.measurmentFromDate = date;
    }

    if (this.measurmentFromDate != null && this.measurmentToDate != null) {
      this.newMeasurmentFromDate = this.measurmentFromDate.year + '-' + this.measurmentFromDate.month + '-' + this.measurmentFromDate.day;

      this.newMeasurmentToDate = this.measurmentToDate.year + '-' + this.measurmentToDate.month + '-' + this.measurmentToDate.day;
    }
  }
  getMeasurmentsByDate() {
    if (this.measurmentFromDate != null && this.measurmentToDate != null) {

      const newFrom = new Date(this.newMeasurmentFromDate).getTime();
      const newTo = new Date(this.newMeasurmentToDate).getTime();

      this.doctorService.getInactive(newFrom, newTo, this.username, this.password).subscribe(response => {
        this.doctors = response;

        this.hideDoctors = false;
      });

    }
  }

  measurmentIsHovered(date: NgbDate) {
    return this.measurmentFromDate && !this.measurmentToDate && this.measurmentHoveredDate && date.after(this.measurmentToDate) && date.before(this.measurmentHoveredDate);
  }

  measurmentIsInside(date: NgbDate) {
    return this.measurmentToDate && date.after(this.measurmentFromDate) && date.before(this.measurmentToDate);
  }

  measurmentIsRange(date: NgbDate) {
    return date.equals(this.measurmentFromDate) || (this.measurmentToDate && date.equals(this.measurmentToDate)) || this.measurmentIsInside(date) || this.measurmentIsHovered(date);
  }

  cLoseMeasurmentTable() {
    this.hideDoctors = true;
  }
}
