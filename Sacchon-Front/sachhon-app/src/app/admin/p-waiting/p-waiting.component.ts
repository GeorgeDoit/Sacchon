import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { PatientService } from 'src/app/services/patient.service';
import { Location } from '@angular/common';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-p-waiting',
  templateUrl: './p-waiting.component.html',
  styleUrls: ['./p-waiting.component.scss']
})
export class PWaitingComponent implements OnInit {

  patients = [];
  waiting = [];
  username: any;
  password: any;

  constructor(private _location: Location,
    private adminService: AdminService,
    private userService: UserService,
    private patientService: PatientService) { }

  ngOnInit() {

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();

    this.getAvailable();
  }


  getAvailable() {
    this.patientService.getAvailableForConsult(this.username, this.password).subscribe(res => {
      this.patients = res;
      var today = new Date();
      this.patients.forEach(element => {
        // const Difference_In_Time = date2.getTime() - date1.getTime();
        var Difference_In_Time = today.getTime() - element.dateAvailableConsulted;

        var Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24);
        this.waiting.push(Difference_In_Days)
      });
    });

  }
  backClicked() {
    this._location.back();
  }
}
