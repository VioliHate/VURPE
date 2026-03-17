import {
  ChangeDetectionStrategy,
  Component,
  inject,
  Injectable,
  model,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AddRuleDialog } from '../components/add-rule-dialog/add-rule-dialog';

@Injectable({
  providedIn: 'root',
})
export class RulesSerivce {
  readonly dialog = inject(MatDialog);

  public addRow() {
    console.log('apro dialogo');
    this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddRuleDialog, {
      width: '80vw',
      height: '60vh',
      maxWidth: '100vw',
      maxHeight: '100vh',
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
      if (result !== undefined) {
        console.log('Dialog result:', result);
      }
    });
  }
}
