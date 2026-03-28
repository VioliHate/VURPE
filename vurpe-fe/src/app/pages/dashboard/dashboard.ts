import { Component, inject, input, signal } from '@angular/core';
import { DragAndDropCsv } from '../../components/drag-and-drop-csv/drag-and-drop-csv';
import { FileService } from '../../services/file-service';
import { DialogService } from '../../services/dialog-service';
import { DashboardServices } from '../../services/dashboard-services';
import { StatsCard } from '../../components/stats-card/stats-card';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-dashboard',
  imports: [DragAndDropCsv, StatsCard, MatIcon],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  private dialog = inject(DialogService);
  fileService = inject(FileService);
  statusMessage = signal('');
  DashboardSrv = inject(DashboardServices);
  list = signal<any>([]);

  constructor() {
    this.DashboardSrv.getStats().subscribe((resp) => {
      let maps = Object.entries(resp);
      this.list.set(maps);
      //console.log(this.list() );
    });
  }

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
