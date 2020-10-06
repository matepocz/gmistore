import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NewMessageRequestModel} from "../../models/messages/new-message-request.model";

export interface MessageDialogData {
  receiver: string;
  subject: string;
  text: string;
}

@Component({
  selector: 'app-message-dialog',
  templateUrl: './message-dialog.component.html',
})
export class MessageDialogComponent implements OnInit {

  messageForm: FormGroup = this.fb.group({
    receiver: [null],
    subject: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(100)])],
    content: [null, Validators.compose(
      [Validators.required, Validators.minLength(10), Validators.maxLength(3000)])]
  });

  message: NewMessageRequestModel;

  constructor(public dialogRef: MatDialogRef<MessageDialogComponent>,
              private fb: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data?: MessageDialogData) { }

  ngOnInit(): void {
    this.messageForm.patchValue({
      receiver: this.data.receiver,
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSubmit(): NewMessageRequestModel{
    this.message = this.messageForm.value;
    return this.message;
  }
}
