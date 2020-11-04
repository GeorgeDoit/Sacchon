import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { AdminService } from 'src/app/services/admin.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-p-wo-activity',
  templateUrl: './p-wo-activity.component.html',
  styleUrls: ['./p-wo-activity.component.scss']
})
export class PWoActivityComponent implements OnInit {

  measurmentHoveredDate: NgbDate;
  measurmentFromDate: NgbDate;
  measurmentToDate: NgbDate;
  newMeasurmentFromDate: string;
  newMeasurmentToDate: string;
  patients = [];
  hidePatients: boolean = true;
  username: any;
  password: any;

  constructor(private _location: Location,
    private userService: UserService,

    private patientService: PatientService) { }

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

      this.patientService.getPatientsNoActive(newFrom, newTo, this.username, this.password).subscribe(response => {
        this.patients = response;
        this.hidePatients = false;
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
    this.hidePatients = true;
  }
}
