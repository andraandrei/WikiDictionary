import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { Auth, getAuth, getIdToken } from '@angular/fire/auth';
import { FormControl, FormGroup, FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';
declare const google: any;

@Component({
  selector: 'app-maps',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  isLoggedIn : boolean;
  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://52.47.141.244:8017/setWord";
  @ViewChild('wordForm') form:NgForm;
  
  constructor(private userService: UsersService,private router: Router,private http: HttpClient) 
  {
    this.formReg = new FormGroup({
      email: new FormControl(),
      password: new FormControl()
    })
   }

  ngOnInit(): void {
    this.noLogueado()
  }

  noLogueado()
  {
    const auth = getAuth();
    const user = auth.currentUser;
    if(this.isLoggeddIn()==true)
    {
      this.router.navigate(['/maps']);
    } 
    else if (this.isLoggeddIn()==false)
    { 
      this.router.navigate(['/login']);
      console.log("error, no ha entrado al perifl ");
      alert("No se ha podido hacer el log-in correctamente");
    }
  }

   // Http Headers
   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  onWordCreate(data){
    console.log(data)
    let body = "";
    body = "{ \"word\": \""+data.pWord+"\",\"definitions\": [\""+data.pDefinition+"\"]}";
    this.http.post(this.url,body,this.httpOptions).subscribe((response: any) => 
    {
      console.log(body)
      console.log(response)
      if(response.Success != null)
      {
        console.log("Success")
        this.success = Array.of(response);
        console.log(this.success)
      } 
      else 
      {
        this.error1 = Array.of(response);
        console.log(this.error1); 
      }
     }); 
  }

  getError():boolean 
  {  
    if(this.error1 !== undefined)
    {
      return true;
    }  
    else 
    {
      return false;
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
    console.log(user)
   if(user)
   {
    this.isLoggedIn = true;
    return true;
   }
   else 
   {
    return false;
   }
  }
   
  @Input() word // Capitalize Input!
  
}
