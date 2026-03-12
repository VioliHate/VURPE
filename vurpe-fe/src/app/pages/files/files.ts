import { Component, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { rxResource } from '@angular/core/rxjs-interop';
import { DynamicTable } from '../../components/dynamic-table/dynamic-table';

import { ApiResponse } from '../../entities/ApiResponse';
import { PageEvent } from '@angular/material/paginator';
import { SortUrl } from '../../utils/sort';
import { Sort } from '@angular/material/sort';
import { DynamicFilters } from '../../components/dynamic-filters/dynamic-filters';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-files',
  imports: [DynamicTable, DynamicFilters, MatButton],
  templateUrl: './files.html',
  styleUrl: './files.scss',
  standalone: true,
})
export class Files {
  private http = inject(HttpClient);

  pageIndex = signal(0);
  pageSize = signal(20);
  sortField = signal('id');
  sortDir = signal('ASC');
  filter = signal(null);

  dataResource = rxResource<any, any>({
    params: () => ({
      page: this.pageIndex(),
      size: this.pageSize(),
      sortField: this.sortField(),
      sortDir: this.sortDir(),
      criteria: this.filter(),
    }),

    stream: ({ params }) => {
      let httpParams: any = this.buildParams(params);

      return this.http.get<ApiResponse<any>>('http://localhost:8080/call/files', {
        params: httpParams,
      });
    },
  });

  private buildParams(params: any) {
    let sort = new SortUrl(params.sortField, params.sortDir);
    let httpParams: any = new HttpParams()
      .set('page', params.page)
      .set('size', params.size)
      .set('sort', sort.toString());

    if (params.criteria != null) {
      for (let iter of Object.keys(params.criteria)) {
        console.log('patate->', iter, ' ', params.criteria[iter]);
        httpParams = httpParams.set(iter, params.criteria[iter]);
      }
      console.log(httpParams);
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
}
