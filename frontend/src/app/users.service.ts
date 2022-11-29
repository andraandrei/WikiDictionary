import { Injectable } from '@angular/core';
import { Auth, createUserWithEmailAndPassword, GoogleAuthProvider, signInWithEmailAndPassword, signInWithPopup, signOut } from '@angular/fire/auth';


@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private auth:Auth) { }

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
    return signOut(this.auth);
  }
}
