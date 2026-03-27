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
  tableData = computed(() => this.data().value()?.payload?.content || []);
  hasError = computed(() => !!this.data().error());
  isRefreshing = computed(() => this.data().isLoading() && !!this.data().value());
  columns = computed(() => {
    const content = this.tableData();
    return content.length > 0 ? Object.keys(content[0]) : [];
  });
  displayedColumns = computed(() => (this.columns().length > 0 ? ['edit', ...this.columns()] : []));
  pageInfo = computed<PageInfo | undefined>(() => this.data().value()?.payload?.page);

  DataSource = new MatTableDataSource<any>();
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor() {
    effect(() => {
      if (!this.hasError() && this.data().value()) {
        this.DataSource.data = this.tableData();
        if (this.sort) this.DataSource.sort = this.sort;
      }
    });
  }

  onPageChange(event: PageEvent) {
    this.pageChanged.emit(event); // Il componente padre ascolterà questo evento per fare la chiamata HTTP
  }

  onSortChange(event: Sort) {
    this.sortChanged.emit(event);
  }
}
