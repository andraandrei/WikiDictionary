import { Injectable } from '@angular/core';
import { Auth, createUserWithEmailAndPassword, GoogleAuthProvider, signInWithEmailAndPassword, signInWithPopup, signOut } from '@angular/fire/auth';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class UsersService {
  isLoggedin: boolean = false;
  constructor(private auth:Auth, private router: Router) { }

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

  error(){
    
  }

  login( {email,password}: any ) {

    try{
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

  isLoggedIn() {

    if (JSON.parse(localStorage.getItem('currentUser')).auth_token == null) {
      this.isLoggedin = false;
      return this.isLoggedin;
    }
    else {
      return true;
    }
  }
}
