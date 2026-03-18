import { Component, effect, inject, Injector, input, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { rxResource } from '@angular/core/rxjs-interop';
import { DynamicTable } from '../../components/dynamic-table/dynamic-table';

import { ApiResponse } from '../../entities/ApiResponse';
import { PageEvent } from '@angular/material/paginator';
import { SortUrl } from '../../utils/sort';
import { Sort } from '@angular/material/sort';
import { DynamicFilters } from '../../components/dynamic-filters/dynamic-filters';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute } from '@angular/router';
import { FileService } from '../../services/file-service';
import { RulesSerivce } from '../../services/rules-serivce';

const SERVICE_REGISTRY: { [key: string]: any } = {
  files: FileService,
  rules: RulesSerivce,
};

@Component({
  selector: 'app-father-manager',
  imports: [DynamicTable, DynamicFilters, MatButton],
  templateUrl: './father-manager.html',
  styleUrl: './father-manager.scss',
  standalone: true,
})
export class FatherManager {
  private http = inject(HttpClient);
  api = input.required<string>();

  private route = inject(ActivatedRoute);
  parentId = this.route.snapshot.queryParamMap.get('file_id');
  serviceKey = input.required<string>();
  pageIndex = signal(0);
  pageSize = signal(20);
  sortField = signal('id');
  sortDir = signal('ASC');
  filter = signal(null);

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
    effect(() => {
      const serviceToken = SERVICE_REGISTRY[this.serviceKey()];

      if (serviceToken) {
        this.Srv = this.injector.get(serviceToken);
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
      console.log(httpParams);
    }
    let url = params.api;
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
    this.filter.set(null);
  }

  async addNewRow() {
    console.log('clicked add new row');
    let result = await this.Srv.addRow();
    result?.subscribe((res: any) => {
      if (res.status == 'OK') {
        this.sortField.set('createdAt');
        this.sortDir.set('DESC');
      }
    });
  }

  detailsRow(el: any) {
    this.Srv.getDetails(el.id);
  }

  deleteRow(el: any) {
    console.log('clicked delete', el);
  }

  editRow(el: any) {
    console.log('clicked edit', el);
  }
}
