import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './user/login/login.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { RegisterComponent } from './patient/register/register.component';
import { AdminComponent } from './admin/admin.component';
import { PatientComponent } from './patient/patient.component';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { DoctorComponent } from './doctor/doctor.component';
import { PSubsComponent } from './admin/p-subs/p-subs.component';
import { DWoActivityComponent } from './admin/d-wo-activity/d-wo-activity.component';
import { PWoActivityComponent } from './admin/p-wo-activity/p-wo-activity.component';
import { PWaitingComponent } from './admin/p-waiting/p-waiting.component';
import { DSubsComponent } from './admin/d-subs/d-subs.component';
import { GetFreePComponent } from './doctor/get-free-p/get-free-p.component';
import { GetMyPComponent } from './doctor/get-my-p/get-my-p.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'admin/:id', component: AdminComponent },
  { path: 'patient/:id', component: PatientComponent },
  { path: 'patient/measurments:id', component: PatientComponent },
  { path: 'manage-account/:id', component: ManageAccountComponent },

  { path: 'doctor/:id', component: DoctorComponent },
  { path: 'doctor/free-patients/:id', component: GetFreePComponent },
  { path: 'doctor/my-patients/:id', component: GetMyPComponent },


  { path: 'admin/patients-submissions/:id', component: PSubsComponent },
  { path: 'admin/doctors-submissions/:id', component: DSubsComponent },

  { path: 'admin/patients-waiting/:id', component: PWaitingComponent },
  { path: 'admin/patients-activity/:id', component: PWoActivityComponent },
  { path: 'admin/doctors-activity/:id', component: DWoActivityComponent },



  { path: "**", component: PageNotFoundComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]

})
export class AppRoutingModule { }
export const routingComponents = [
  PageNotFoundComponent,
  LoginComponent,
  RegisterComponent,
  ManageAccountComponent,

  PatientComponent,

  DoctorComponent,
  GetFreePComponent,
  GetMyPComponent,
  AdminComponent,

  PSubsComponent,
  DSubsComponent,
  PWaitingComponent,
  PWoActivityComponent,
  DWoActivityComponent
];
