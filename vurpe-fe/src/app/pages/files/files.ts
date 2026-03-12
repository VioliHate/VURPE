import {Component, inject, signal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {rxResource} from '@angular/core/rxjs-interop';
import {DynamicTable} from '../../component/dynamic-table/dynamic-table';

import {ApiResponse} from '../../entities/ApiResponse';
import {PageEvent} from '@angular/material/paginator';
import {Sort} from '@angular/material/sort';

@Component({
  selector: 'app-files',
  imports: [
    DynamicTable
  ],
  templateUrl: './files.html',
  styleUrl: './files.scss',
  standalone: true
})
export class Files {
  private http = inject(HttpClient);

  pageIndex = signal(0);
  pageSize = signal(10);


  dataResource = rxResource<any, any>({

    params: () => ({
      page: this.pageIndex(),
      size: this.pageSize(),

    }),

    stream: ({params}) => {

      let httpParams: any = new HttpParams()
        .set('page', params.page)
        .set('size', params.size);


      return this.http.get<ApiResponse<any>>('http://localhost:8080/call/files', {params: httpParams});
    }
  });

  onPageChange(event: PageEvent) {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
  }

  onSortChange(event: Sort) {
    //   if (!event.active || event.direction === '') {
    //     this.sortField.set('id');
    //     this.sortDir.set('asc');
    //   } else {
    //     this.sortField.set(event.active);
    //     this.sortDir.set(event.direction);
    //   }
    //   this.pageIndex.set(0);
    // }
  }
}
