import { Component, effect, input, computed, ViewChild, output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { PageInfo } from '../../entities/PageInfo';
import { CamelToTitlePipe } from '../../pipe/CamelToTitlePipe';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dynamic-table',
  imports: [
    CommonModule,
    MatButtonModule,
    MatMenuModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    CamelToTitlePipe,
    MatIcon,
    MatButton,
  ],
  templateUrl: './dynamic-table.html',
  styleUrl: './dynamic-table.scss',
  standalone: true,
})
export class DynamicTable {
  serviceKey = input.required<string>();
  data = input.required<any>();
  plainDisplayedColumns = signal<string[]>([]);

  pageInfo = computed<PageInfo | undefined>(() => {
    return this.data().payload?.page;
  });

  pageChanged = output<PageEvent>();
  sortChanged = output<any>();
  refreshCall = output<null>();

  selectedDetails = output<any>();
  selectedDelete = output<any>();
  selectedEdit = output<any>();
  selectedNew = output<null>();
  viewGraph = output<any>();
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
  displayedColumns = computed(() => ['edit', ...this.columns()]);

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
