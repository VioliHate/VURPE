import { Component, effect, input, computed, ViewChild, output, signal } from '@angular/core';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { PageInfo } from '../../entities/PageInfo';
import { CamelToTitlePipe } from '../../pipe/CamelToTitlePipe';
import { DynamicFilters } from '../dynamic-filters/dynamic-filters';
import { MatIcon } from "@angular/material/icon";

@Component({
  selector: 'app-dynamic-table',
  imports: [CommonModule, MatTableModule, MatSortModule, MatPaginatorModule, CamelToTitlePipe, MatIcon],
  templateUrl: './dynamic-table.html',
  styleUrl: './dynamic-table.scss',
  standalone: true,
})
export class DynamicTable {
  data = input.required<any>();
  plainDisplayedColumns = signal<string[]>([]);
  pageInfo = computed<PageInfo | undefined>(() => {
    return this.data().payload?.page;
  });
  pageChanged = output<PageEvent>();
  sortChanged = output<any>();
  refreshCall = output<any>();

  customColumns = input<string[]>([]);
  columns = computed(() => {
    const custom = this.customColumns();
    if (custom.length > 0) return custom;

    const tableData = this.data().payload.content;
    if (tableData) {
      return tableData.length > 0 ? Object.keys(tableData[0]) : [];
    }
    return [];
  });
  displayedColumns = computed(() => ['edit',...this.columns() ]);
  
  

  DataSource = new MatTableDataSource<any>();

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      this.DataSource.data = this.data().payload.content;

      if (this.sort) this.DataSource.sort = this.sort;
      if (this.paginator) this.DataSource.paginator = this.paginator;
      this.plainDisplayedColumns.set(this.displayedColumns());
    });
  }
  onPageChange(event: PageEvent) {
    this.pageChanged.emit(event); // Il componente padre ascolterà questo evento per fare la chiamata HTTP
  }
  onSortChange(event: Sort) {
    this.sortChanged.emit(event);
  }
}
