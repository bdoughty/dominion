import {Component, Input} from '@angular/core';
const VISIBLE_FOR_TIME = 3000;

@Component({
  selector: 'dmn-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent {
  public visible = false;
  @Input() public text = '';

  ngOnChanges() {
    if (this.text !== '') {
      this.visible = true;

      setTimeout(() => { // Hide it again
        this.hide();
      }, VISIBLE_FOR_TIME);
    }
  }

  hide() {
    this.visible = false;
    this.text = '';
  }

}

