import { Component, inject } from '@angular/core';
import { DragAndDropCsv } from '../../components/drag-and-drop-csv/drag-and-drop-csv';
import { FileService } from '../../services/file-service';
import { DialogService } from '../../services/dialog-service';

@Component({
  selector: 'app-dashboard',
  imports: [DragAndDropCsv],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  private dialog = inject(DialogService);
  fileService = inject(FileService);

  constructor() {}

  onFileProcessed(file: File) {
    this.fileService.callUploadCSV(file)?.subscribe((res: any) => {
      if (res.status == 'OK') {
        this.dialog.success('File caricato correttamente');
      } else {
        this.dialog.error('Errore nel caricamento del file');
      }
    });
  }
}
