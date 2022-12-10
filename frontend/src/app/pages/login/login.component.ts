import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';
import { getAuth } from "firebase/auth";
import { Observable } from 'rxjs';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  isLoggedIn : boolean;
  formLogin: FormGroup;
  admin:boolean
  firebase:any
  
  constructor(private userService: UsersService,private router: Router) 
  {
    this.formLogin = new FormGroup({email: new FormControl(),password: new FormControl()})
  }

  public isAdmin()
  {
    let admin = getAuth().currentUser.email
    if(admin.match('admin@gmail.com'))
    {
      return true;
    }
    else 
    {
      return false;
    }
  }
  onSubmit() 
  {
    this.userService.login(this.formLogin.value).then(response => 
    {
      console.log(response);
      if (this.isLoggeddIn()==true) 
      {
        this.router.navigate(['/user-profile']);
      } 
      if (this.isLoggeddIn()==true && !this.isAdmin())
      {
        alert("Loggeado correctamente, redirigiendo a su perfil")
        this.router.navigate(['/user-profile']);  
      }
      else if (this.isLoggeddIn()==false)
      { 
        this.router.navigate(['/login']);
        console.log("error, no ha entrado al perifl de ysyari")
      }
    })
    .catch(error =>  alert("No se ha podido hacer el log-in correctamente. Error: " + error));  
  }

  onClick()
  {
    this.userService.loginWithGoogle().then(response => 
    {
      console.log(response);
      if(this.isLoggeddIn()==true)
      {
        alert("Loggeado correctamente, redirigiendo a su perfil")
        this.router.navigate(['/user-profile']);
      }
      else if (this.isLoggeddIn()==false)
      { 
        this.router.navigate(['/login']);
        console.log("error, no ha entrado al perifl de ysyari")
      }
    })
    .catch(error =>  alert("No se ha podido hacer el log-in correctamente. Error: " + error));
  }

  ngOnInit() {

  }
  ngOnDestroy() {
  }

  isLoggeddIn()
  {
    const auth = getAuth();
    const user = auth.currentUser;
    if(user)
    {
      this.isLoggedIn = true;
      return true;
    }
    else {
      return false;
    } 
  }

}
