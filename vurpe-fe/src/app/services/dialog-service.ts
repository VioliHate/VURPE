import { inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ResultDialog, DialogData } from '../components/result-dialog/result-dialog';

@Injectable({ providedIn: 'root' })
export class DialogService {
  private dialog = inject(MatDialog);

  success(message: string) {
    this.#open('success', message, 'Operazione Riuscita');
  }

  error(message: string) {
    this.#open('error', message, 'Operazione Fallita');
  }

  #open(type: 'success' | 'error', message: string, title?: string) {
    this.dialog.open(ResultDialog, {
      width: '420px',
      maxWidth: '90vw',
      panelClass: 'vurpe-dialog-panel',
      backdropClass: 'vurpe-dialog-backdrop',
      data: { type, message, title } as DialogData,
      autoFocus: false,
    });
  }
}
