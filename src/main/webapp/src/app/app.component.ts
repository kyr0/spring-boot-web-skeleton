import {Component} from '@angular/core';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {MenuComponent} from './components/menu/menu.component';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [MenuComponent, DashboardComponent],
})
export class AppComponent {
  title = 'app works!';
}
