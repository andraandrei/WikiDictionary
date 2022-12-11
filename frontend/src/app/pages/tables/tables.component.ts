import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { getAuth } from '@angular/fire/auth';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';

@Component({
  selector: 'app-update',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {

  isLoggedIn : boolean;
  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://52.47.141.244:8017/updateWord";
  constructor(private userService: UsersService,private router: Router,private http: HttpClient) 
  {
    this.formReg = new FormGroup({
      email: new FormControl(),
      password: new FormControl()
    });
   }

  ngOnInit(): void 
  {
    if(this.isAdmin()==true)
    {
      this.router.navigate(['/tables']);
    } 
    else 
    {
      if(this.isLoggeddIn()==true)
      {
        this.router.navigate(['/user-profile']);
        console.log("error, no es admin");
        alert("No tiene permisos de moderador");
      } 
      else if(this.isLoggeddIn()==false)
      {
        this.router.navigate(['/login']);
        console.log("error, no es admin");
      }     
    }   
  }
   // Http Headers
   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  onWordUpdate(data)
  {
    console.log(data);
    let body = "";
    body = "{ \"word\": \""+data.pWord+"\",\"definitions\": [\""+data.pDefinition+"\"]}";   
    this.http.put(this.url, body,this.httpOptions).subscribe((response: any) => { 
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

  isLoggeddIn()
  {
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
  

isAdmin()
{  
  const auth = getAuth();
  const user = auth.currentUser;
  if(user)
  {
    if(user.email.match('admin@gmail.com'))
    {
      return true;
    }
  }
  else 
  {
    return false;
  }
}
  @Input() word // Capitalize Input!
}
