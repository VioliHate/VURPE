import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);

  public getDetails(id: string) {
    this.router.navigate(['/dataRecords'], { queryParams: { file_id: id } });
  }

  public addRow() {
    let file = this.openWindowFileCSV();
    this.http.post('http://localhost:8080/call/uploadCSV', file).subscribe((res) => {
      console.log(res);
    });
  }

  private async openWindowFileCSV() {
    try {
      // Apre la finestra di selezione file
      const [fileHandle] = await (window as any).showOpenFilePicker({
        types: [
          {
            description: 'File CSV',
            accept: {
              'text/csv': ['.csv'],
            },
          },
        ],
        multiple: false,
      });

      const file = await fileHandle.getFile();

      //console.log('File selezionato:', file.name, file.size, file.type);
      return file;
    } catch (err: any) {
      if (err.name === 'AbortError') {
        return;
      }
      console.error('Error on opening:', err);
    }
  }
}
