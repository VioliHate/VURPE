import {Component, effect, input, computed, ViewChild, viewChild} from '@angular/core';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import {ApiResponse} from '../../entities/ApiResponse';

@Component({
  selector: 'app-dynamic-table',
  imports: [
    CommonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    TitleCasePipe
    ],
  templateUrl: './dynamic-table.html',
  styleUrl: './dynamic-table.scss',
})
export class DynamicTable {

  data=input.required<ApiResponse<any>>();

  customColumns = input<string[]>([]);
  columns = computed(()=>{
    const custom=this.customColumns();
    if(custom.length>0) return custom;

    const tableData = this.data().payload.content;
    return tableData.length > 0 ? Object.keys(tableData[0]) : [];
  });

  DataSource = new MatTableDataSource<any>();

  @ViewChild(MatSort) sort! : MatSort;
  @ViewChild(MatPaginator) paginator! : MatPaginator;

  constructor(){
    effect(()=>{
      this.DataSource.data = this.data().payload.content;
      console.log("ciaooooo");
      console.log(this.DataSource.data);

      if(this.sort) this.DataSource.sort = this.sort;
      if(this.paginator) this.DataSource.paginator = this.paginator;

    })
  }



}
