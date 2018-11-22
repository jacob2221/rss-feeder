import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs-compat/Observable";
import {AccordionModule} from "ngx-accordion";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'RSS Aggregator';
  allFeeds : any;
  feeds : any;
  today: number = Date.now();
  constructor(private httpClient:HttpClient) {}
  
  ngOnInit() {
	    this.getAllFeeds();
        setInterval(() => this.getAllFeeds(), 10000);
		
    }
	
	private getAllFeeds() {
      this.httpClient.get("http://localhost:9010/rssfeed/api/getAllFeeds/").subscribe(data => {this.allFeeds = data});
    }
}
