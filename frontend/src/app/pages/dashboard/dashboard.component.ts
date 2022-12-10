import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { getAuth } from '@angular/fire/auth';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import Chart from 'chart.js';
import { UsersService } from 'app/users.service';

// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2
} from "../../variables/charts";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  
  constructor( private router: Router, private http: HttpClient, private userService: UsersService){

  }
  public datasets: any;
  public data: any;
  public salesChart;
  public clicked: boolean = true;
  public clicked1: boolean = false;

  ngOnInit() {

    this.datasets = [
      [0, 20, 10, 30, 15, 40, 20, 60, 60],
      [0, 20, 5, 25, 10, 30, 15, 40, 40]
    ];
    this.data = this.datasets[0];


    var chartOrders = document.getElementById('chart-orders');

    parseOptions(Chart, chartOptions());


    var ordersChart = new Chart(chartOrders, {
      type: 'bar',
      options: chartExample2.options,
      data: chartExample2.data
    });

    var chartSales = document.getElementById('chart-sales');

    this.salesChart = new Chart(chartSales, {
			type: 'line',
			options: chartExample1.options,
			data: chartExample1.data
		});
  }


  public updateOptions() {
    this.salesChart.data.datasets[0].data = this.data;
    this.salesChart.update();
  }

  wordData: any = [];
  palabra: string ="";
  url: string = "";
  definitions: any
  word1: any
  siError:boolean=false;
  error1: any

  logged: boolean;
  formReg: FormsModule;

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
