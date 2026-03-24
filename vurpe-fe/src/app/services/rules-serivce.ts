import { inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddRuleDialog } from '../components/add-rule-dialog/add-rule-dialog';
import { HttpClient } from '@angular/common/http';
import { BusinessRule } from '../entities/BusinessRule';
import { environment } from '../../environments/environment';
import { of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RulesSerivce {
  readonly dialog = inject(MatDialog);
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;

async addRow() {
  // Restituiamo il risultato di openDialog per poterlo usare nel component
  return this.openDialog();
}

openDialog() {
  const dialogRef = this.dialog.open(AddRuleDialog, {
    width: '80vw',
      height: '60vh',
      maxWidth: '100vw',
      maxHeight: '100vh',
  });

  // Trasformiamo l'evento di chiusura in una chiamata API
  return dialogRef.afterClosed().pipe(
    switchMap((data: BusinessRule) => {
      if (data) {
        console.log('Dialog result:', data);
        return this.sendRule(data); // Ritorna l'observable della POST
      } else {
        return of(null); // Se l'utente chiude senza salvare
      }
    })
  );
}

  sendRule(data: BusinessRule) {
  
      return this.http.post(`${this.apiUrl}/rules/add`, data)
  }
}
