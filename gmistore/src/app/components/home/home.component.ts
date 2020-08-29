import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  slides = [
    {'image': 'https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/landscaping-ideas-1582321830.jpg'},
    {'image': 'https://www.yourtrainingedge.com/wp-content/uploads/2019/05/background-calm-clouds-747964-1068x674.jpg'},
    {'image': 'https://static.photocdn.pt/images/articles/2019/02/07/Simple_Landscape_Photography_Tips_With_Tons_of_Impact.jpg'},
    {'image': 'https://syndlab.com/files/view/5db9b150252346nbDR1gKP3OYNuwBhXsHJerdToc5I0SMLfk7qlv951730.jpeg'},
    {'image': 'https://d2rdhxfof4qmbb.cloudfront.net/wp-content/uploads/20180522150453/frozen-lake-baikal-1068x712.jpg'}];

  constructor() {
  }

  ngOnInit(): void {

  }

}
