import { Component, OnInit, ElementRef, Input } from '@angular/core';
import { ROUTES } from '../sidebar/sidebar.component';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { getAuth } from "firebase/auth";
import { UsersService } from 'src/app/users.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss','./nicepage.css']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  constructor(location: Location,  private element: ElementRef, 
    private router: Router, private http: HttpClient, private userService: UsersService) {
    this.location = location;
  }

  //////////////////////////////////
  wordData: any = [];
  palabra: string ="";
  url: string = "";
  definitions: any
  word1: any
  siError:boolean=false;
  error1: any

  logged: boolean;
  formReg: FormsModule


  inputValue(value){
    this.url = 'http://127.0.0.1:8017/getWord?word='+value;

    const httpOptions = {
 	 	  headers: new HttpHeaders()
	  }

    httpOptions.headers.append('Access-Control-Allow-Origin', '*');
    httpOptions.headers.append('Content-Type', 'application/json');
    httpOptions.headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    
    this.http.get<any>(this.url).subscribe(data => {
        this.error1=data.error;
        this.getError();
        this.wordData = Array.of(data);
        console.log(this.wordData)
 
    }) ;


  }


  getError():boolean {

   
    
    if(this.error1 !== undefined){
      return true;
    }
      
    else {
      return false;
    }
  }
////////////////////////////////////////////////

  ngOnInit() {
    this.listTitles = ROUTES.filter(listTitle => listTitle);
    var html = document.getElementsByTagName("html")[0];
    html.classList.add("auth-layout");
    var body = document.getElementsByTagName("body")[0];
    body.classList.add("bg-default");
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
   });

   
  }
  getTitle(){
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if(titlee.charAt(0) === '#'){
        titlee = titlee.slice( 1 );
    }

    for(var item = 0; item < this.listTitles.length; item++){
        if(this.listTitles[item].path === titlee){
            return this.listTitles[item].title;
        }
    }
    return 'Dashboard';
  }
  test: Date = new Date();
  public isCollapsed = true;

  

                                       
  ngOnDestroy() {
    var html = document.getElementsByTagName("html")[0];
    html.classList.remove("auth-layout");
    var body = document.getElementsByTagName("body")[0];
    body.classList.remove("bg-default");
  }

  isLoggedIn(){
    const auth = getAuth();
    const user = auth.currentUser;

   if(user)
   {
    this.logged = true;
   }

   return true;
   console.log(user)
    
  }

  logout(){
    this.userService.logout()
      .then(() => {
        this.router.navigate(['/dashboard']);
      })
      .catch(error => console.log(error));
  }
  @Input() word // Capitalize Input!
}

