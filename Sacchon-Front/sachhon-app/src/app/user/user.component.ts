import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'sachhon-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  userForm: FormGroup;
  users = [];
  isHidden: boolean = true;


  constructor(private userService: UserService) { }

  ngOnInit() { }

  // public getUsers() {
  //   this.userService.getUser().subscribe(response => {
  //     this.users = response;
  //   });
  //   this.isHidden = !this.isHidden;

  // }
}
