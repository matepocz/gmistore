import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {Observable} from "rxjs";
import {AuthService} from "../service/auth-service";
import {map} from "rxjs/operators";

export function usernameValidator(authService: AuthService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return authService.checkIfUsernameTaken(control.value).pipe(
      map(res => {
        return res ? {usernameExists: true} : null;
      })
    )
  }
}

