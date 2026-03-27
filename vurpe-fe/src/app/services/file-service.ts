import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { TabConfig } from '../data/TabConfig';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private router = inject(Router);
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;

  public tabsConfig: TabConfig = {
    title: 'file',
    columns: ['id', 'fileSize'],
    buttons: ['Modifica', 'Elimina', 'Visualizza', 'Metriche'],
    new: true,
  };

  public getDetails(el: any) {
    this.router.navigate(['/dataRecords'], {
      queryParams: { file_id: el.id, file_status: el.status },
    });
  }
  public getMetrics(id: string) {
    this.router.navigate(['/metrics'], { queryParams: { file_id: id } });
  }

  // FileService

  public async addRow() {
    // Aggiungiamo il return qui
    return await this.openWindowFileCSV();
  }

  private async openWindowFileCSV() {
    try {
      const [fileHandle] = await (window as any).showOpenFilePicker({
        types: [{ description: 'File CSV', accept: { 'text/csv': ['.csv'] } }],
        multiple: false,
      });

      const file = await fileHandle.getFile();

      // Fondamentale: devi fare il RETURN del risultato di callUploadCSV
      return this.callUploadCSV(file);
    } catch (err: any) {
      if (err.name === 'AbortError') {
        return null; // L'utente ha chiuso la finestra senza scegliere
      }
      console.error('Error on opening:', err);
      return null;
    }
  }

  public callUploadCSV(file: any) {
    const formData = new FormData();
    formData.append('file', file);
    // Questo restituisce un Observable
    return this.http.post(`${this.apiUrl}/uploadCSV`, formData);
  }
}
