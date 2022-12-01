import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/users.service';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  formReg: FormGroup;
  errorMessage : string;
  url = "https://dictionaryapp-b279b-default-rtdb.europe-west1.firebasedatabase.app/users.json";

  constructor(
    private userService: UsersService,
    private router: Router, private http:HttpClient
    ) {
      this.formReg = new FormGroup({
        email: new FormControl(),
        password: new FormControl()
      })
   }
  
  ngOnInit(): void {
  }
  

  onSubmit(){
    this.userService.registro(this.formReg.value)
    .then(response => {
      console.log(response);
      this.router.navigate(['/login']);
    })
    .catch(error =>  alert("No se ha podido hacer el registro correctamente. Error: " + error));
    
}

onClick() {
  this.userService.loginWithGoogle()
    .then(response => {
      console.log(response);
      this.router.navigate(['/login']);
    })
    .catch(error =>  alert("No se ha podido hacer el registro correctamente. Error: " + error))
}

onCreate(loginForm:{ inputword:string, inputdef:string }){
  console.log(loginForm);
  const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'users/json'
    })
} 
 
  httpOptions.headers.append('Access-Control-Allow-Origin', '*');
  httpOptions.headers.append('Content-Type', 'application/json');
  httpOptions.headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
  return this.http.post(this.url,loginForm,httpOptions)
  .subscribe((response)=> {
    console.log(response);
  });
}



}
