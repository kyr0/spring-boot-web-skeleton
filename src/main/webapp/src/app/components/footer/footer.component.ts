import {Component, OnInit, ViewChild} from '@angular/core';

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

    public year = new Date().getFullYear();

    constructor() {
    }

    ngOnInit() {
    }


}
