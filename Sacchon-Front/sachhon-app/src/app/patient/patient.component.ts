import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, NumberValueAccessor, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, ModalDismissReasons, NgbDate, NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { IConsult } from '../models/consult';
import { IPatientMeasurment } from '../models/patientMeasurment';
import { PatientService } from '../services/patient.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'sachhon-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.scss']
})
export class PatientComponent implements OnInit {

  private routeSub: Subscription;
  patientId: number;

  addMeasurmentForm: FormGroup;
  editMeasurmentForm: FormGroup;
  hideMeasurments = true;
  hideConsults = true;
  hideStats = true;

  measurmentHoveredDate: NgbDate;
  measurmentFromDate: NgbDate;
  measurmentToDate: NgbDate;
  newMeasurmentFromDate: string;
  newMeasurmentToDate: string;
  glucoseLvl: any;
  carbLvl: any;
  username: string;
  password: string;


  constructor(
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router,
    private patientService: PatientService,
    private modalService: NgbModal,
    private userService: UserService

  ) {

    this.addMeasurmentForm = new FormGroup({
      glucose: new FormControl('',
        [Validators.required,
        Validators.pattern("^[0-9]+.*$")]),
      carbs: new FormControl('',
        [Validators.required,
        Validators.pattern("^[0-9]+.*$")])
    });

    this.editMeasurmentForm = new FormGroup({
      glucose: new FormControl('',
        [Validators.required,
        Validators.pattern("^[0-9]+.*$")]),
      carbs: new FormControl('',
        [Validators.required,
        Validators.pattern("^[0-9]*$")])
    });

  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      this.patientId = params['id'];
    });

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();

    this.patientService.warnPat(this.patientId, this.username, this.password).subscribe(res => {
      const warn: any = res
      if (warn.warning) {
        this.toastr.warning("Warning!", "A consultation has been updated!");
      }
    })
  }

  async addMeasurmentFormSubmitted() {
    const measurment: IPatientMeasurment = {
      carbIntake: this.addMeasurmentForm.get('carbs').value,
      glucoseLevel: this.addMeasurmentForm.get('glucose').value,
      dateStored: new Date().getTime()
    }

    this.patientService.addMeasurment(measurment, this.patientId, this.username, this.password).subscribe(res => {
      this.toastr.success("Success", "Measurment added")

    }, error => {
      this.toastr.error("Error", "Measurment error")

    });
  }
  measurments: any = [];

  getMeasurments() {

    this.patientService.getMeasurments(this.patientId, this.username, this.password).subscribe(response => {
      this.measurments = response;
    });
  }

  getMeasurmentsByDate() {
    if (this.measurmentFromDate != null && this.measurmentToDate != null) {

      const newFrom = new Date(this.newMeasurmentFromDate).getTime();
      const newTo = new Date(this.newMeasurmentToDate).getTime();

      this.patientService.getMeasurmentsByDate(this.patientId, newFrom, newTo, this.username, this.password).subscribe(response => {
        this.measurments = response;

        if (this.measurments.length === 0) {
          this.toastr.info("info", "You dont have any measurments")
          this.hideMeasurments = true;

        } else {
          this.hideMeasurments = false;
        }
      });
    } else {
      this.toastr.info("info", "You need to select date range first")

    }
  }

  closeResult = '';
  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'MeasurmentModal' }).result.then((result) => {
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

  measurmentId: number;
  openModal(targetModal, measurment) {

    this.measurmentId = measurment.id;

    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

    this.editMeasurmentForm.patchValue({
      glucose: measurment.glucoseLevel,
      carbs: measurment.carbIntake
    });
  }

  modalFormSubmit() {
    this.modalService.dismissAll();

    const res = this.editMeasurmentForm.getRawValue();
    const newMeasurment: IPatientMeasurment = {
      carbIntake: res.carbs,
      glucoseLevel: res.glucose,
      dateStored: new Date().getTime()
    };

    this.patientService.updateMeasurment(newMeasurment, this.measurmentId, this.username, this.password).subscribe(res => {
      this.toastr.success("Success", "Measurment updated");

    }, error => {
      this.toastr.error("Error", "Measurment update",);

    });
    this.getMeasurments();
  }

  deleteMeasurment() {
    this.patientService.deleteMeasurment(this.measurmentId, this.username, this.password).subscribe(res => {
      this.toastr.warning("Success", "Measurment deleted")

    }, error => {
      this.toastr.error("Error", "Measurment error")

    });
    this.getMeasurments();

  }

  avgStats = [];
  getDailyStats() {
    this.patientService.getAvgStats(this.patientId, this.username, this.password).subscribe(response => {
      this.avgStats.push(response);
      console.log(this.avgStats)
      if (this.avgStats.length < 1) {

        this.toastr.info("Info", "No daily stats yet")
      }
    });

    this.hideStats = false;

  }

  closeStats() {
    this.hideStats = true;
  }

  consults: any = [];
  getConsults() {
    this.patientService.getConsults(this.patientId, this.username, this.password).subscribe(response => {
      this.consults = response;
      if (this.consults.length === 0) {
        this.toastr.info("Info", "You have no cunsultations yet")
      } else {
        this.hideConsults = false;
      }
    });

  }

  closeConsults() {
    this.hideConsults = true;
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
    this.hideMeasurments = true;
  }

  manageAccount() {
    this.userService.setUserRole('patient');
    this.router.navigate(['manage-account/' + this.patientId]);
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }

  logout() {
    this.router.navigate(['login']);
  }

}
