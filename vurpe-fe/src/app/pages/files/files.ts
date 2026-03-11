import {Component, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {rxResource} from '@angular/core/rxjs-interop';
import {DynamicTable} from '../../component/dynamic-table/dynamic-table';
import {catchError, map, tap} from 'rxjs';
import {ApiResponse} from '../../entities/ApiResponse';

@Component({
  selector: 'app-files',
  imports: [
    DynamicTable
  ],
  templateUrl: './files.html',
  styleUrl: './files.scss',
})
export class Files {
  private http = inject(HttpClient);

  dataResource = rxResource({
      stream: () =>this.http.get<ApiResponse<any>>('http://localhost:8080/call/files')
    }
  )


}
