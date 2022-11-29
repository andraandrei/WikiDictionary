import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  
  api = 'http://127.0.0.1:8017/getWord?word=';
  input:string="cat";
  url: any;
  static inputValue: any;
  
  constructor (private http:HttpClient) {}

  
 
  getCountries(){
    
    const httpOptions = {
 	 	headers: new HttpHeaders()
    
	}

    httpOptions.headers.append('Access-Control-Allow-Origin', '*');
    httpOptions.headers.append('Content-Type', 'application/json');
    httpOptions.headers.append('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');

  
    return this.http.get(this.url,httpOptions); 
    
    
    }
  }
