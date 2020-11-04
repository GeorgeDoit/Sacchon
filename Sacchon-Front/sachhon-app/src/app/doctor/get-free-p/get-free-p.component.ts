
import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DoctorService } from 'src/app/services/doctor.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'sachhon-get-free-p',
  templateUrl: './get-free-p.component.html',
  styleUrls: ['./get-free-p.component.scss']
})
export class GetFreePComponent implements OnInit {
  freePatients: any = [];
  hidePatients: boolean = true;

  addConsultForm: FormGroup;
  updateConsultForm: FormGroup;

  private routeSub: Subscription;
  doctorId: any;
  patientId: any;
  username: string;
  password: string;

  patientData: any = [];
  patientConsults: any = [];
  hideData: boolean = true;
  hideConsults: boolean = true;
  hideAl: boolean = true;

  constructor(
    private _location: Location,
    private toastr: ToastrService,
    private patientService: PatientService,
    private doctorService: DoctorService,
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

    this.patientId = freePatient.id;
    this.modalService.open(targetModal, {
      centered: true,
      backdrop: 'static'
    });

  }

  modalFormSubmit() {
    this.modalService.dismissAll();

    const consult = {
      title: this.addConsultForm.get('title').value,
      description: this.addConsultForm.get('description').value,
    }

    this.doctorService.postConsult(this.doctorId, this.patientId, consult, this.username, this.password).subscribe(res => {
      this.toastr.success("Success!", "Coonsultation sent!")
    }, error => {
      this.toastr.error("Error!", "Consultation error")

    });

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

  backClicked() {
    this._location.back();
  }


}
