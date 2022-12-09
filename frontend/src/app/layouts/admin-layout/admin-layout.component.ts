import { Component, OnInit } from '@angular/core';
import { Auth, getAuth, signOut } from '@angular/fire/auth';
import { Router } from '@angular/router';
import { UsersService } from 'app/users.service';


@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss','./nicepage.css']
})
export class AdminLayoutComponent implements OnInit {

  isLoggedin: boolean = false;
  test: Date = new Date();
  public isCollapsed = true;
  logged: boolean;
  
  constructor(private router: Router, public userService: UsersService,private auth:Auth) { }

  ngOnInit() {

    
    var html = document.getElementsByTagName("html")[0];
    html.classList.add("auth-layout");
    var body = document.getElementsByTagName("body")[0];
    body.classList.add("bg-default");
    this.router.events.subscribe((event) => {
      this.isCollapsed = true;
   });

  }
  ngOnDestroy() {
    var html = document.getElementsByTagName("html")[0];
    html.classList.remove("auth-layout");
    var body = document.getElementsByTagName("body")[0];
    body.classList.remove("bg-default");
  }

  
  isLoggeddIn(){
    const auth = getAuth();
    const user = auth.currentUser;
    

   if(user)
   {
    console.log(user)
    console.log(user.email)
    this.logged = true;
    return true;
   }

   else {
    return false;
   }

    
  }

  logouut(){
    this.userService.logout()
      .then(() => {
        this.router.navigate(['/dashboard']);
      })
      .catch(error => console.log(error));

}

logout() {

  return signOut(this.auth)
  .then(response => {
    console.log(response);
    this.router.navigate(['/dashboard']);
  })
  .catch(error =>  alert("Ha ocurrido un error. Error: " + error));
}

noAdmin(){
  const auth = getAuth();
    const user = auth.currentUser;
    if(user){
    // if (!user.email.match('admin@gmail.com')){
      this.logged = true;
      return true;
}

   else {
    return false;
   }

}



  isAdmin(){
    
    const auth = getAuth();
    const user = auth.currentUser;
    if(user){
      if(user.email.match('admin@gmail.com')){
        return true;
      }
    }
    else {
      return false;
    }
  }
}
