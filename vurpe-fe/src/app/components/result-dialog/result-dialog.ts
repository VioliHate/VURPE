import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

export interface DialogData {
  type: 'success' | 'error';
  message: string;
  title?: string;
}

@Component({
  selector: 'app-result-dialog',
  imports: [CommonModule, MatDialogModule, MatButtonModule],
  templateUrl: './result-dialog.html',
  styleUrl: './result-dialog.scss',
})
export class ResultDialog {
  readonly dialogRef = inject(MatDialogRef<ResultDialog>);
  readonly data = inject<DialogData>(MAT_DIALOG_DATA);

  close() {
    this.dialogRef.close();
  }
}
