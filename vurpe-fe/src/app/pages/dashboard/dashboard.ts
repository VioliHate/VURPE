import { Component, inject } from '@angular/core';
import { DragAndDropCsv } from '../../components/drag-and-drop-csv/drag-and-drop-csv';
import { FileService } from '../../services/file-service';

@Component({
  selector: 'app-dashboard',
  imports: [DragAndDropCsv],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  fileService = inject(FileService);

  constructor() {}

  onFileProcessed(file: File) {
    this.fileService.callUploadCSV(file)?.subscribe((res: any) => {
      if (res.status == 'OK') {
        //success dialog di caricamento riuscito
      } else {
        //error dialog di caricamento fallito
      }
    });
  }
}
