import { Injectable } from '@angular/core';
import { Auth, createUserWithEmailAndPassword, getAuth, GoogleAuthProvider, signInWithEmailAndPassword, signInWithPopup, signOut } from '@angular/fire/auth';
import { Router } from '@angular/router';
// import { observable } from 'rxjs';
import { of as observableOf } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UsersService {
  isLoggedin: boolean = false;
  usuario:string;
  constructor(private auth:Auth, private router: Router) { }

  // uid = observableOf('123');
  // isAdmin = observableOf(true);

  ifAdmin(){
  
  }

  registro({email,password}: any){

    try{
      return  createUserWithEmailAndPassword(this.auth,email,password);
    }
    catch(error) {
      alert("No se ha podido hacer el registro correctamente. Error: " + error)
      console.log("No se ha podido hacer el registro correctamente. Error: " + error);
      return null;
    }
  }





  login( {email,password}: any ) {

    try{
      console.log("userService.login funciona")
      return signInWithEmailAndPassword(this.auth, email, password);
  }
  catch(error) {
      alert("No se ha podido hacer el log-in correctamente. Error: " + error)
      console.log("No se ha podido hacer el log-in correctamente. Error: " + error);
      return null;
  }
}     



  loginWithGoogle() {
    return signInWithPopup(this.auth, new GoogleAuthProvider());
  }

  logout() {

    return signOut(this.auth)
    .then(response => {
      console.log(response);
      this.router.navigate(['/dashboard']);
    })
    .catch(error =>  alert("Ha ocurrido un error. Error: " + error));
    
  }

  retornarUsuario(){
    this.usuario = JSON.parse(localStorage.getItem('currentUser'))
    return this.usuario;
    
  }


  isAdmin(){
    let admin = getAuth().currentUser.email

    if(admin.match('admin@gmail.com')){
      return true;
    }
     else {
      return false;
     }
  }
}
