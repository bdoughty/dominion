import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'dmn-creation',
  templateUrl: './creation-page.component.html',
  styleUrls: ['./creation-page.component.css']
}) export class CreationComponent implements OnInit {
  title = 'app works!';
  public blowUp = document.getElementById("blowUp");

  ngOnInit() {
    console.log("hello");
    // for(let i = 0; i < 20; i++) {
    //   document.getElementById("card" + i).addEventListener("onmouseenter", event => {
    //     this.blowUp.innerHTML = "<img class='image' id='blowUpImg' src='../../assets/card" + i + ".jpg' />";
    //     document.getElementById("blowUpImg").style.visibility = "visible";
    //     console.log("mouse entered " + i);
    //   });
    //
    //   document.getElementById("card" + i).addEventListener("onmouseleave", event => {
    //     document.getElementById("blowUpImg").style.visibility = "hidden";
    //   });
    // }
  }
}


