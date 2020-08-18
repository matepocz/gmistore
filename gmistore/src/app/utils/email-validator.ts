import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {Observable} from "rxjs";
import {AuthService} from "../service/auth-service";
import {map} from "rxjs/operators";

export function emailValidator(authService: AuthService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return authService.checkIfEmailTaken(control.value).pipe(
      map(res => {
        return res ? {emailTaken: true} : null;
      })
    )
  }
}
