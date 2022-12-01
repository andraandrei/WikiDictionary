import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/users.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  obj: object;
  wordData: any = [];
  success:any;
  formReg: FormGroup;
  siError:boolean=false;
  error1: any
  url: string = "http://localhost:8017/setWord";
  user:string;
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
  }

  onClick() {
    this.userService.logout()
      .then(() => {
        this.router.navigate(['/dashboard']);
      })
      .catch(error => console.log(error));
  }

  
   
  @Input() word // Capitalize Input!


}
