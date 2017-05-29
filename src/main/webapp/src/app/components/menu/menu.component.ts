import {Component, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  public isMobileActive = false;

  constructor() {
  }

  ngOnInit() {
  }

  click() {
   this.isMobileActive = !this.isMobileActive;
  }
}
