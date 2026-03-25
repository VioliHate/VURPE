import {
  Component,
  effect,
  inject,
  input,
  output,
  PLATFORM_ID,
  signal,
  untracked,
} from '@angular/core';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { CamelToTitlePipe } from '../../pipe/CamelToTitlePipe';
import { MatOption, MatSelect } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { TabConfig } from '../../data/TabConfig';
import { isPlatformBrowser, KeyValuePipe } from '@angular/common';

@Component({
  selector: 'app-dynamic-filters',
  imports: [
    MatFormField,
    MatLabel,
    CamelToTitlePipe,
    MatSelect,
    MatOption,
    ReactiveFormsModule,
    MatInput,
    MatButton,
    MatIcon,
    KeyValuePipe,
  ],
  templateUrl: './dynamic-filters.html',
  styleUrl: './dynamic-filters.scss',
  standalone: true,
})
export class DynamicFilters {
  private platformId = inject(PLATFORM_ID);

  list2 = input.required<any>();
  conf = input.required<TabConfig>();

  list: any[] = [];
  filtersMap = signal<Map<any, any>>(new Map());

  MapOutput = output<any>();

  constructor() {
    effect(() => {
      const content = this.list2().payload?.content;
      if (content && content.length > 0) {
        const keys = Object.keys(content[0]);
        const columns = this.conf().columns || [];
        this.list = keys.filter((item) => !columns.includes(item));
      } else {
        this.list = [];
      }
    });

    effect(() => {
      const tableId = this.conf().title;
      const storageKey = `filters_${tableId}`;

      if (isPlatformBrowser(this.platformId)) {
        const saved = localStorage.getItem(storageKey);
        if (saved) {
          try {
            const parsed = JSON.parse(saved);
            untracked(() => {
              this.filtersMap.set(new Map(Object.entries(parsed)));
            });
          } catch (e) {
            console.error('Errore parsing filtri salvati:', e);
            untracked(() => this.filtersMap.set(new Map()));
          }
        } else {
          untracked(() => this.filtersMap.set(new Map()));
        }
      }
    });
  }

  private saveToStorage(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const tableId = this.conf().title;
    const storageKey = `filters_${tableId}`;
    const current = this.filtersMap();

    if (current.size > 0) {
      localStorage.setItem(storageKey, JSON.stringify(Object.fromEntries(current)));
    } else {
      localStorage.removeItem(storageKey);
    }
  }

  addMap(key: any, value: any): void {
    if (!key || value === undefined || value === '') return;

    this.filtersMap.update((oldMap) => {
      const newMap = new Map(oldMap);
      newMap.set(key, value);
      return newMap;
    });

    this.saveToStorage();
  }

  deleteFromMap(key: any): void {
    this.filtersMap.update((oldMap) => {
      const newMap = new Map(oldMap);
      newMap.delete(key);
      return newMap;
    });

    this.saveToStorage();
  }

  send(): void {
    const filters = Object.fromEntries(this.filtersMap());
    this.MapOutput.emit(filters);

    this.saveToStorage();
  }
}
