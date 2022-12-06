import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Auth, getAuth, getIdToken } from '@angular/fire/auth';
import { FormControl, FormGroup, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/users.service';
declare const google: any;

@Component({
  selector: 'app-maps',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss','./nicepage.css']
})
export class PostComponent implements OnInit {


  isLoggedIn : boolean;
  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://localhost:8017/setWord";
  
  
  constructor(
    private userService: UsersService,
    private router: Router,private http: HttpClient
    ) {
      this.formReg = new FormGroup({
        email: new FormControl(),
        password: new FormControl()
      })
   }

  ngOnInit(): void {

    const auth = getAuth();
    const user = auth.currentUser;
    if(this.isLoggeddIn()==true){
      this.router.navigate(['/maps']);
    } 
    else if (this.isLoggeddIn()==false){ 
      this.router.navigate(['/login']);
      console.log("error, no ha entrado al perifl de ysyari");
      alert("No se ha podido hacer el log-in correctamente");
    }
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
    console.log(user)

   if(user)
   {
    this.isLoggedIn = true;
    return true;
   }

   else {
    return false;
   }

  }
  
  returnUser(){
    const auth = getAuth();
    const user = auth.currentUser;
    user.getIdToken
    console.log(user)

}
   
  @Input() word // Capitalize Input!
  
}
