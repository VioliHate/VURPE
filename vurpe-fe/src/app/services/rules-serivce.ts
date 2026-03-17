import { inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddRuleDialog } from '../components/add-rule-dialog/add-rule-dialog';
import { HttpClient } from '@angular/common/http';
import { BusinessRule } from '../entities/BusinessRule';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RulesSerivce {
  readonly dialog = inject(MatDialog);
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;

  public addRow() {
    //console.log('apro dialogo');
    this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(AddRuleDialog, {
      width: '80vw',
      height: '60vh',
      maxWidth: '100vw',
      maxHeight: '100vh',
    });

    dialogRef.afterClosed().subscribe((data: BusinessRule) => {
      //console.log('The dialog was closed');
      if (data !== undefined) {
        //console.log('Dialog result:', data);
        this.sendRule(data);
      }
    });
  }

  sendRule(data: BusinessRule) {
    try {
      console.log(data);
      this.http.post(`${this.apiUrl}/rules/add`, data).subscribe((resp) => {
        if (resp) {
          console.log(resp);
        }
      });
    } catch (err: any) {
      console.error('Error :', err);
    }
  }
}
