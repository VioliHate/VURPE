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
import { isPlatformBrowser } from '@angular/common';

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
      if (this.list2().payload.content != 0) {
        let val = Object.keys(this.list2().payload.content[0]);

        let col = this.conf().columns;

        this.list = val.filter((item) => !col.includes(item));
      }
    });

    effect(() => {
      // Recuperiamo un identificativo unico dalla config (es. il titolo o una proprietà 'id')
      const tableId = this.conf().title;
      const storageKey = `filters_${tableId}`;

      if (isPlatformBrowser(this.platformId)) {
        const saved = localStorage.getItem(storageKey);
        if (saved) {
          try {
            const parsed = JSON.parse(saved);
            // Usiamo untracked per impostare il valore senza triggerare immediatamente il salvataggio
            untracked(() => {
              this.filtersMap.set(new Map(Object.entries(parsed)));
            });
          } catch (e) {
            this.filtersMap.set(new Map());
          }
        } else {
          // Se non c'è nulla nel cache per questa tabella, svuotiamo i filtri correnti
          untracked(() => this.filtersMap.set(new Map()));
        }
      }
    });

    effect(() => {
      const tableId = this.conf().title;
      const storageKey = `filters_${tableId}`;
      const currentFilters = this.filtersMap();

      if (isPlatformBrowser(this.platformId)) {
        if (currentFilters.size > 0) {
          const obj = Object.fromEntries(currentFilters);
          localStorage.setItem(storageKey, JSON.stringify(obj));
        } else {
          // Se i filtri sono vuoti, puliamo il localStorage per questa tabella
          localStorage.removeItem(storageKey);
        }
      }
    });
  }

  addMap(key: any, value: any) {
    this.filtersMap.update((oldMap) => {
      const newMap = new Map(oldMap);
      newMap.set(key, value);
      return newMap;
    });
  }

  deleteFromMap(key: any) {
    this.filtersMap.update((oldMap) => {
      const newMap = new Map(oldMap);
      newMap.delete(key);
      return newMap;
    });
  }

  send() {
    this.MapOutput.emit(Object.fromEntries(this.filtersMap()));
  }
}
