import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {Location, Appearance, GermanAddress} from "@angular-material-extensions/google-maps-autocomplete";
import PlaceResult = google.maps.places.PlaceResult;
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-orders-product-details',
  templateUrl: './orders-product-details.component.html',
  styleUrls: ['./orders-product-details.component.css']
})
export class OrdersProductDetailsComponent implements OnInit {
  dataSource: any;
  displayedColumns = ['name', 'email', 'phone'];
  form: FormGroup;
  public appearance = Appearance;
  public zoom: number;
  public latitude: number;
  public longitude: number;
  public selectedAddress: PlaceResult;


  constructor(private changeDetection: ChangeDetectorRef) {
  }


  addressFormGroup: FormGroup;

  ngOnInit(): void {
    this.addressFormGroup = new FormGroup({
      address: new FormControl(),
    });

    this.zoom = 10;
    this.latitude = 52.520008;
    this.longitude = 13.404954;

    this.setCurrentPosition();

    this.addressFormGroup.patchValue({
      address: {
        postalCode: "121212",
        displayAddress: "Budapest, Hungary",
      }
    })

    this.addressFormGroup.get('address').valueChanges.subscribe(value => console.log('value changed', value))
  }

  private setCurrentPosition() {
    if ('geolocation' in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        this.zoom = 12;
      });
    }
  }

  onGermanAddressMapped($event: GermanAddress) {
    console.log('onGermanAddressMapped', $event.geoLocation.latitude);
    this.latitude = $event.geoLocation.latitude;
    this.longitude = $event.geoLocation.longitude;

    console.log(this.addressFormGroup.value)
  }

}
