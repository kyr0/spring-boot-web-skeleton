import {Component, OnInit} from '@angular/core';
import {MenuComponent} from '../menu/menu.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css', './featurebox.css'],
  providers: [MenuComponent]
})
export class DashboardComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
