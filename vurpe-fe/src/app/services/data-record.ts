import { Injectable } from '@angular/core';
import { TabConfig } from '../data/TabConfig';

@Injectable({
  providedIn: 'root',
})
export class DataRecord {
  public tabsConfig: TabConfig = {
    title: 'data-record',
    columns: ['id', 'file_id'],
    buttons: [],
    new: false,
  };
}
