import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { getAuth } from '@angular/fire/auth';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  isLoggedIn : boolean;
  formReg: FormGroup;
  usuario:string;

  constructor(
    private userService: UsersService,private router: Router,private http: HttpClient) 
    {
      this.formReg = new FormGroup({
        email: new FormControl(),
        password: new FormControl()
      })
   }

   // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  ngOnInit() 
  {
    const auth = getAuth();
    const user = auth.currentUser;
    console.log(user);
    if(this.isLoggeddIn()==true)
    {
      this.router.navigate(['/user-profile']);
    } 
    else if (this.isLoggeddIn()==false)
    { 
      this.router.navigate(['/login']);
      console.log("error, no ha entrado al perifl de ysyari");
      alert("No se ha podido hacer el log-in correctamente");
    }
  }

  onClick() 
  {
    this.userService.logout().then(() => 
    {
      this.router.navigate(['/dashboard']);
    })
    .catch(error => console.log(error));
  }

  isLoggeddIn(){
    const auth = getAuth();
    const user = auth.currentUser;    
    if(user)
    {
      this.isLoggedIn = true;
      return true;
    }
    else 
    {
      return false;
    }
  return user;    
  }

  returnUser()
  {
    const auth = getAuth();
    const user = auth.currentUser;
    this.usuario=user.email
    return this.usuario;   
  }

}
