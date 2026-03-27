import {
  Component,
  effect,
  input,
  computed,
  ViewChild,
  output,
  ResourceStatus,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { PageInfo } from '../../data/PageInfo';
import { CamelToTitlePipe } from '../../pipe/CamelToTitlePipe';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatFabButton } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { TabConfig } from '../../data/TabConfig';
import { StatusBadgeDirective } from '../../directives/status-badge-directive';
import { MatDivider } from '@angular/material/divider';
import { MatTooltip } from '@angular/material/tooltip';

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
    StatusBadgeDirective,
    MatFabButton,
    MatDivider,
    MatTooltip,
  ],
  templateUrl: './dynamic-table.html',
  styleUrl: './dynamic-table.scss',
  standalone: true,
})
export class DynamicTable {
  serviceKey = input.required<string>();
  data = input.required<any>();
  config = input<TabConfig>({
    title: '',
    columns: [],
    buttons: [],
    new: true,
  });

  pageChanged = output<PageEvent>();
  sortChanged = output<any>();
  refreshCall = output<null>();

  selectedDetails = output<any>();
  selectedDelete = output<any>();
  selectedEdit = output<any>();
  selectedNew = output<any>();
  viewGraph = output<any>();

  // computed

  hasError = computed(() => !!this.data().error());
  columns = computed(() => {
    if (this.hasError()) return [];
    const val = this.data().value();
    const tableData = val?.payload?.content || [];
    return tableData.length > 0 ? Object.keys(tableData[0]) : [];
  });
  displayedColumns = computed(() => (this.columns().length > 0 ? ['edit', ...this.columns()] : []));

  pageInfo = computed<PageInfo | undefined>(() => {
    if (this.hasError()) return undefined;
    return this.data().value()?.payload?.page;
  });

  DataSource = new MatTableDataSource<any>();
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      if (this.hasError()) {
        this.DataSource.data = [];
        return;
      }
      const val = this.data().value();
      if (val) {
        this.DataSource.data = val.payload.content;
      }
      if (this.sort) this.DataSource.sort = this.sort;
    });
  }

  onPageChange(event: PageEvent) {
    this.pageChanged.emit(event); // Il componente padre ascolterà questo evento per fare la chiamata HTTP
  }

  onSortChange(event: Sort) {
    this.sortChanged.emit(event);
  }
}
