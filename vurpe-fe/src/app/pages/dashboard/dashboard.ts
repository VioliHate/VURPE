import { Component, effect, inject, Injector } from '@angular/core';
import { DragAndDropCsv } from '../../components/drag-and-drop-csv/drag-and-drop-csv';
import { FileService } from '../../services/file-service';
import { RulesSerivce } from '../../services/rules-serivce';

const SERVICE_REGISTRY: { [key: string]: any } = {
  files: FileService,
  rules: RulesSerivce,
};

@Component({
  selector: 'app-dashboard',
  imports: [DragAndDropCsv],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  services: any = null;
  private injector = inject(Injector);

  constructor() {
    effect(() => {
      const serviceToken = SERVICE_REGISTRY['files'];

      if (serviceToken) {
        this.services = this.injector.get(serviceToken);
      } else {
        console.warn('Service non trovato');
      }
    });
  }

  onFileProcessed(file: File) {
    console.log(file);
    this.services.callUploadCSV(file)?.subscribe((res: any) => {
      if (res.status == 'OK') {
        console.log('faricato');
      }
    });
  }
}
