import { DOCUMENT } from '@angular/common';
import { Location } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Button } from 'protractor';
import { Subscription } from 'rxjs';
import { DoctorService } from 'src/app/services/doctor.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'sachhon-get-my-p',
  templateUrl: './get-my-p.component.html',
  styleUrls: ['./get-my-p.component.scss']
})
export class GetMyPComponent implements OnInit {

  myPatients: any = [];
  hidePatients: boolean = true;

  addConsultForm: FormGroup;
  updateConsultForm: FormGroup;

  private routeSub: Subscription;
  doctorId: any;
  patientId: any;
  username: string;
  password: string;
  consults: any = [];
  consultId: any;

  patientData: any = [];
  patientConsults: any = [];
  hideData: boolean = true;
  hideConsults: boolean = true;
  hideAl: boolean = true;
  patientDataId: any;
  patientConsultId: any;
  patientConsultName: any;
  patientConsultSurname: any;
  patientDataName: any;
  patientDataSurname: any;
  seen: any = [];
  hideModal = false;

  constructor(
    private _location: Location,
    private toastr: ToastrService,
    private patientService: PatientService,
    private modalService: NgbModal,
    private router: Router,
    private userService: UserService,
    private route: ActivatedRoute,
    private doctorService: DoctorService) { }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe(params => {
      this.doctorId = params['id'];
    });

    this.addConsultForm = new FormGroup({
      title: new FormControl('',
        [Validators.required,
        ]),
      description: new FormControl('',
        [Validators.required,
        ])
    });

    this.updateConsultForm = new FormGroup({
      title: new FormControl('',
        [Validators.required,
        ]),
      description: new FormControl('',
        [Validators.required,
        ])
    });

    this.username = this.userService.getUserName();
    this.password = this.userService.getPassword();

    this.getPatients();

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

  addConsultModal(targetModal, myPatient) {

    this.patientId = myPatient.id;

    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

    this.addConsultForm.patchValue({
      title: this.addConsultForm.get('title').value,
      description: this.addConsultForm.get('description').value
    });
  }

  updateConsultModal(targetModal, myPatient) {

    this.patientId = myPatient.id;
    const update = 'update_' + myPatient.id;

    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

    this.doctorService.getConsults2(this.doctorId, this.patientId, this.username, this.password).subscribe(res => {
      this.consults = res;

      this.consults.forEach(element => {

        this.consultId = element.id;
        let seen: any = {
          id: myPatient.id,
          seen: element.seen
        }

        this.seen.push(seen);

        if (element.seen === false) {
          this.updateConsultForm.patchValue({
            title: element.title,
            description: element.description
          });
        } else {
          this.toastr.warning('Warning!', 'User already seen this cunsultation!');
          (document.getElementById(update) as HTMLButtonElement).disabled = true;
        }
      });
    });

  }

  addModalFormSubmit() {

    this.modalService.dismissAll();

    const res = this.addConsultForm.getRawValue();
    const consult = {
      title: res.title,
      description: res.description
    };

    this.doctorService.postConsult(this.doctorId, this.patientId, consult, this.username, this.password).subscribe()

    this.toastr.success("Success!", "Consultation sent");
  }

  updateModalFormSubmit() {

    this.modalService.dismissAll();

    const res = this.updateConsultForm.getRawValue();
    const consult = {
      title: res.title,
      description: res.description
    };

    this.doctorService.putConsult(this.consultId, consult, this.username, this.password).subscribe(response => {
      this.toastr.success("Consultation updated", "Success!")

    }, error => {
      this.toastr.error("Error!", "You cant sent that consutation")
    });

  }

  getPatients() {
    this.patientService.getPatientByDoctor(this.doctorId, this.username, this.password).subscribe(res => {
      this.myPatients = res;
    });
  }

  getMyPatients() {

    if (this.myPatients.length > 0) {
      this.hidePatients = false;
    }

    this.myPatients.forEach(patient => {
      const add = 'add_' + patient.id;
      const update = 'update_' + patient.id;

      if (patient.availableConsulted) {
        (document.getElementById(add) as HTMLButtonElement).disabled = false;

        // add button = disabled;

      } else {
        (document.getElementById(update) as HTMLButtonElement).disabled = false;

        // update - button = disabled;
      }
    });

  }

  closeMyPatientsTable() {
    this.hidePatients = true;
  }

  manageAccount() {
    this.userService.setUserRole('doctor');
    this.router.navigate(['manage-account/' + this.doctorId]);
  }

  backClicked() {
    this._location.back();
  }


  browseData(patient) {
    this.patientDataName = patient.name;
    this.patientDataSurname = patient.surname;

    this.patientService.getMeasurments(patient.id, this.username, this.password).subscribe(res => {
      this.patientData = res;

      if (this.patientData.length > 0) {
        this.hideData = false;
      } else {
        this.hideData = true;
        this.toastr.warning('Warning', "Patient " + patient.name + ' ' + patient.surname + " has no data")
      }
    })

  }

  hideAll() {
    this.hideData = true;
  }

  browseConsults(patient) {
    this.patientConsultName = patient.name;
    this.patientConsultSurname = patient.surname;

    this.doctorService.getConsults2(this.doctorId, patient.id, this.username, this.password).subscribe(async res => {
      this.patientConsults = res;

      if (this.patientConsults.length > 0) {
        this.hideConsults = false;
      } else {
        this.hideConsults = true;
        this.toastr.warning('Warning', "Patient:" + patient.name + ' ' + patient.surname + " has no consultations")
      }
    });


  }

  closeConsult() {
    this.hideConsults = true;
  }

}
