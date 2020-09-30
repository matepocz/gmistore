import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private imageUrl = environment.apiUrl + 'api/images';

  constructor(private httpClient: HttpClient) {
  }

  public async uploadImage(image: File) {
    const uploadData = new FormData();
    uploadData.append('picture', image);

    return await this.httpClient.post(this.imageUrl + '/upload', uploadData).toPromise();
  }

  destroyImage(url: string): Observable<boolean> {
    return this.httpClient.put<boolean>(this.imageUrl, url);
  }
}
