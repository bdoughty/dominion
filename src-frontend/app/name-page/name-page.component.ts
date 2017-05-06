import {Component, OnInit} from '@angular/core';
import {NameSocketService} from "../shared/namesocket.service";

@Component({
  selector: 'dmn-name-page',
  templateUrl: './name-page.component.html',
  styleUrls: ['./name-page.component.css']
})
export class NamePageComponent implements OnInit {
  private name: string = '';

  constructor(public _nameSocketService: NameSocketService) {
  }

  public isValid(name: string) {
    return name.length > 0 && name.length < 20 && /^[\x00-\x7F]*$/.test(name);
  }

  ngOnInit() {
    this._nameSocketService.addListener('redirect', (messageString) => {
      window.location.replace(messageString);
    });
  }

  submit() {
    if (this.isValid(this.name)) {
      this._nameSocketService.send('changename', this.name);
    }
  }
}
