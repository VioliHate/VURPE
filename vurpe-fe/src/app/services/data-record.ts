import { Injectable } from '@angular/core';
import { TabConfig } from '../entities/TabConfig';

@Injectable({
  providedIn: 'root',
})
export class DataRecord {
    public tabsConfig: TabConfig = 
        {
          columns: ['id', "file_id"],
          buttons:[],
          new: false
        };


  
}
