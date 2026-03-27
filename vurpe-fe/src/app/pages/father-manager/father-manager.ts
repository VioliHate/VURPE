import { Component, effect, inject, Injector, input, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { rxResource } from '@angular/core/rxjs-interop';
import { DynamicTable } from '../../components/dynamic-table/dynamic-table';

import { ApiResponse } from '../../data/ApiResponse';
import { PageEvent } from '@angular/material/paginator';
import { SortUrl } from '../../utils/sort';
import { Sort } from '@angular/material/sort';
import { DynamicFilters } from '../../components/dynamic-filters/dynamic-filters';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute } from '@angular/router';
import { FileService } from '../../services/file-service';
import { RulesSerivce } from '../../services/rules-serivce';
import { MetricsService } from '../../services/metrics-service';
import { DialogService } from '../../services/dialog-service';
import { TabConfig } from '../../data/TabConfig';
import { DataRecord } from '../../services/data-record';
import { MatIcon } from '@angular/material/icon';
import { Analizer } from '../../components/analizer/analizer';

const SERVICE_REGISTRY: { [key: string]: any } = {
  files: FileService,
  rules: RulesSerivce,
  metrics: MetricsService,
  dataRecord: DataRecord,
};

@Component({
  selector: 'app-father-manager',
  imports: [DynamicTable, DynamicFilters, MatButton, MatIcon, Analizer],
  templateUrl: './father-manager.html',
  styleUrl: './father-manager.scss',
  standalone: true,
})
export class FatherManager {
  private http = inject(HttpClient);
  api = input.required<string>();
  private dialog = inject(DialogService);

  private route = inject(ActivatedRoute);
  parentId: any = this.route.snapshot.queryParamMap.get('file_id');
  parentStatus: string | undefined;
  serviceKey = input.required<string>();
  pageIndex = signal(0);
  pageSize = signal(20);
  sortField = signal('id');
  sortDir = signal('ASC');
  filter = signal({});
  config: TabConfig = { title: '', columns: [], buttons: [], new: true };

  private injector = inject(Injector);

  //Services
  Srv: any = null;

  dataResource = rxResource<any, any>({
    params: () => ({
      page: this.pageIndex(),
      size: this.pageSize(),
      sortField: this.sortField(),
      sortDir: this.sortDir(),
      criteria: this.filter(),
      api: this.api(),
      parentId: this.parentId,
    }),

    stream: ({ params }) => {
      let httpParams: any = this.buildParams(params);

      return this.http.get<ApiResponse<any>>(params.api, {
        params: httpParams,
      });
    },
  });

  constructor() {
    const navigation = window.history.state;
    this.parentStatus = navigation.file_status;
    effect(() => {
      const serviceToken = SERVICE_REGISTRY[this.serviceKey()];

      if (serviceToken) {
        this.Srv = this.injector.get(serviceToken);
        this.config = this.Srv.tabsConfig; // Assuming each service has a tabsConfig property
      } else {
        console.warn(`Service non trovato per la chiave: ${this.serviceKey()}`);
      }
    });
  }

  private buildParams(params: any) {
    let sort = new SortUrl(params.sortField, params.sortDir);
    let httpParams: any = new HttpParams()
      .set('page', params.page)
      .set('size', params.size)
      .set('sort', sort.toString());

    if (params.criteria != null) {
      for (let iter of Object.keys(params.criteria)) {
        httpParams = httpParams.set(iter, params.criteria[iter]);
      }
    }
    if (params.parentId) {
      httpParams = httpParams.set('file_id', params.parentId);
    }
    return httpParams;
  }

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
  }

  onSortChange(event: Sort) {
    if (!event.active || event.direction === '') {
      this.sortField.set('id');
      this.sortDir.set('asc');
    } else {
      this.sortField.set(event.active);
      this.sortDir.set(event.direction);
    }
    this.pageIndex.set(0);
  }

  sendFilters(event: any) {
    this.filter.set(event);
  }

  private snakeToCamel(str: string): string {
    if (!str) return '';
    return str.replace(/_([a-z0-9])/gi, (match, letter) => letter.toUpperCase());
  }

  refresh() {
    this.pageIndex.set(0);
    this.pageSize.set(20);
    this.sortField.set('id');
    this.sortDir.set('ASC');
    this.deleteFilter(this.config.title);
  }

  async addNewRow(id?: string) {
    const result$ = id ? await this.Srv.addRow(this.parentId) : await this.Srv.addRow();
    // result$ sarà l'Observable restituito dalla POST (o null se annullato)

    // Controllo se result$ esiste (se l'utente ha annullato la scelta file, sarà null)
    if (result$) {
      result$.subscribe({
        next: (res: any) => {
          if (res.status === 'OK') {
            this.dialog.success('File caricato correttamente');
            this.sortField.set('id');
            this.sortDir.set('DESC');
          } else {
            this.dialog.error('Errore nel caricamento del file');
          }
        },
        error: (err: any) => {
          this.dialog.error('Errore di rete o del server');
          console.error(err);
        },
      });
    }
  }

  detailsRow(el: any) {
    this.Srv.getDetails(el);
  }

  viewGraph(el: any) {
    this.Srv.getMetrics(el.id);
  }

  deleteRow(el: any) {
    console.log('clicked delete', el);
  }

  editRow(el: any) {
    console.log('clicked edit', el);
  }

  private deleteFilter(tableName: string) {
    const storageKey = `filters_${tableName}`;
    localStorage.removeItem(storageKey);
    this.filter.set({ reset: true, timestamp: Date.now() });
  }
}
