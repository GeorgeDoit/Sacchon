import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { PatientService } from '../services/patient.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'sachhon-doctor',
  templateUrl: './doctor.component.html',
  styleUrls: ['./doctor.component.scss']
})
export class DoctorComponent implements OnInit {

  freePatients: any = [];
  hidePatients: boolean = true;

  addConsultForm: FormGroup;
  updateConsultForm: FormGroup;

  private routeSub: Subscription;
  doctorId: any;
  username: string;
  password: string;

  constructor(
    private patientService: PatientService,
    private modalService: NgbModal,
    private router: Router,
    private userService: UserService,
    private route: ActivatedRoute) { }

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
  }

  getDoctorsPatients() {

    this.patientService.getStateFreePat(this.username, this.password).subscribe(res => {
      this.freePatients = res;
      if (this.freePatients.length > 0) {
        this.hidePatients = false;
      }
    });
  }

  closeResult = '';
  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'freepatientModal' }).result.then((result) => {
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

  openFreePatientsModal(targetModal, freePatient) {


    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

  }

  modalFormSubmit() {
    this.modalService.dismissAll();

    const res = this.addConsultForm.getRawValue();

  }

  openMyPatientsModal(targetModal, myPatient) {


    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

  }

  modalForm2Submit() {
    this.modalService.dismissAll();

    const res = this.addConsultForm.getRawValue();

  }


  getFreePatients() {
    this.patientService.getStateFreePat(this.username, this.password).subscribe(res => {
      this.freePatients = res;
      if (this.freePatients.length > 0) {
        this.hidePatients = false;
      }
    });
  }

  closeFreePatientsTable() {
    this.hidePatients = true;
  }

  manageAccount() {
    this.userService.setUserRole('doctor');
    this.router.navigate(['manage-account/' + this.doctorId]);
  }

  logout() {
    this.router.navigate(['login']);
  }

  goToFreePatients() {
    this.router.navigate(['doctor/free-patients/' + this.doctorId]);
  }

  goToMyPatients() {
    this.router.navigate(['doctor/my-patients/' + this.doctorId]);
  }


}
