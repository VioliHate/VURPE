import {Component, effect, input, output} from '@angular/core';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {CamelToTitlePipe} from '../../pipe/CamelToTitlePipe';
import {MatOption, MatSelect} from '@angular/material/select';
import {FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

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
    MatIcon
  ],
  templateUrl: './dynamic-filters.html',
  styleUrl: './dynamic-filters.scss',
  standalone: true
})
export class DynamicFilters {
  list2=input.required<any>();
  list:any[]=[];
   filtersMap:Map<any, any>=new Map<any,any>();
    MapOutput=output<any>();
  constructor(){

    effect(()=> {
      this.list=Object.keys(this.list2().payload.content[0]);
    })


  }
addMap(key:any,value:any){
    this.filtersMap.set(key,value);
}
  deleteFromMap(key:any){
    this.filtersMap.delete(key);
  }
  invia(){
    this.MapOutput.emit(Object.fromEntries(this.filtersMap));
  }
}
