import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/users.service';
import { getAuth } from "firebase/auth";
import { Observable } from 'rxjs';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  isLoggedIn : Observable<boolean>;
  formLogin: FormGroup;
  constructor(
    private userService: UsersService,
    private router: Router
  ) {
    this.formLogin = new FormGroup({
      email: new FormControl(),
      password: new FormControl()
    })
  }


  onSubmit() {
    this.userService.login(this.formLogin.value)
      .then(response => {
        console.log(response);
        this.router.navigate(['/user-profile']);
      })
      .catch(error =>  alert("No se ha podido hacer el log-in correctamente. Error: " + error));
  }

  onClick() {
    this.userService.loginWithGoogle()
      .then(response => {
        console.log(response);
        this.router.navigate(['/user-profile']);
      })
      .catch(error =>  alert("No se ha podido hacer el log-in correctamente. Error: " + error))
  }

  ngOnInit() {
    const auth = getAuth();
    const user = auth.currentUser;
    
 

  }
  ngOnDestroy() {
  }

  onText(){

    const auth = getAuth();
    const user = auth.currentUser;

    if (user) {
      console.log(user)
      // User is signed in, see docs for a list of available properties
      // https://firebase.google.com/docs/reference/js/firebase.User
      // ...
    } else {
      console.log("errorrrr")
    }

  }

}
