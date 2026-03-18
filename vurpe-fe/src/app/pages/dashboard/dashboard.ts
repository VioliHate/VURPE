import { Component, inject, input, signal } from '@angular/core';
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
  statusMessage = signal('');

  constructor() {}

  onFileProcessed(file: File) {
    this.fileService.callUploadCSV(file).subscribe({
      next: () => this.dialog.success('File caricato correttamente'),
      error: () => {
        this.statusMessage.set('Errore nel CSV');
        this.dialog.error('Errore nel caricamento del file');
      },
    });
  }
}
