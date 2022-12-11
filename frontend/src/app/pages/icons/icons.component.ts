
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { getAuth } from '@angular/fire/auth';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';

@Component({
  selector: 'app-icons',
  templateUrl: './icons.component.html',
  styleUrls: ['./icons.component.scss']
})
export class IconsComponent implements OnInit {
  isLoggedIn : boolean;
  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://localhost:8017/deleteWord";
  
  constructor(private userService: UsersService,private router: Router,private http: HttpClient) 
  {
    this.formReg = new FormGroup({
      email: new FormControl(),
      password: new FormControl()  
    })
   }

  ngOnInit(): void {
    if(this.isAdmin()==true)
    {
      this.router.navigate(['/icons']);
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
    }),
  };
  
  onWordDelete(data)
  { 
    let options = 
    {
      headers: new HttpHeaders(
      {
        'Content-Type': 'application/json',
      }),
      body: 
      {
        "word": data.pWord
      },
    };
    this.http.delete(this.url, options).subscribe((response) => 
    {
      console.log(response);
      if(response != null)
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
    console.log(data);
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
    }).catch(error => console.log(error));
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


