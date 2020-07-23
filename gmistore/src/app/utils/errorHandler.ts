import {FormGroup} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";

export function errorHandler(error, form: FormGroup) {
  if (error instanceof HttpErrorResponse && error.status === 400) {
    for (const fieldError of error.error.fieldErrors) {
      const formControl = form.get(fieldError.field);
      if (formControl) {
        formControl.setErrors({serverError: fieldError.message});
      }
    }
  }
}
