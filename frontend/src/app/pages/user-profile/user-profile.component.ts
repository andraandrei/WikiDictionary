import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { getAuth } from '@angular/fire/auth';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/users.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss','./nicepage.css']
})
export class UserProfileComponent implements OnInit {

  isLoggedIn : boolean;
  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://localhost:8017/setWord";
  usuario:string;
  constructor(
    private userService: UsersService,
    private router: Router,private http: HttpClient
    ) {
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

  onWordCreate(words:{word:string, definitions:string[]}){
  
    console.log(words);

    let body = "";
    body = "{ \"word\": \""+words.word+"\",\"definitions\": [\""+words.definitions+"\"]}";
    this.http.post(this.url,body,this.httpOptions).subscribe((response: any) => {

      

      if(response.Success != null)
      {
        console.log("Success")
        
       this.success = Array.of(response);
       console.log(this.success)

      } else {
        

        this.error1 = Array.of(response);
        console.log(this.error1);
        
      }

     });
   
  }

  getError():boolean {
    
    if(this.error1 !== undefined){
      return true;
    }
      
    else {
      return false;
    }
  }

  ngOnInit() {
    const auth = getAuth();
    const user = auth.currentUser;
    console.log(user);
    // this.returnPosts();
    if(this.isLoggeddIn()==true){
      this.router.navigate(['/user-profile']);
    } 
    else if (this.isLoggeddIn()==false){ 
      this.router.navigate(['/login']);
      console.log("error, no ha entrado al perifl de ysyari");
      alert("No se ha podido hacer el log-in correctamente");
    }
  }

  onClick() {
    this.userService.logout()
      .then(() => {
        this.router.navigate(['/dashboard']);
      })
      .catch(error => console.log(error));
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
      error =>  alert("Ha ocurrido un error. Error: " + error);
      console.log("errorrrr")
    }

  }

  isLoggeddIn(){
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

   return user;
    
  }

  returnUser(){
    const auth = getAuth();
    const user = auth.currentUser;
    this.usuario=user.email
    return this.usuario;
    
  }
  
   
  @Input() word // Capitalize Input!


}
