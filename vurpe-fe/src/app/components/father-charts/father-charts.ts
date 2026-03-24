import { Component, computed, inject, input, signal } from '@angular/core';
import { Charts } from "../charts/charts";
import { rxResource } from '@angular/core/rxjs-interop';
import { HttpClient } from '@angular/common/http';
import { ApiResponse } from '../../entities/ApiResponse';
import { tap } from 'rxjs';
import { TitleCasePipe } from '@angular/common';
import { MatSelect, MatOption } from "@angular/material/select";
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-father-charts',
  standalone: true,
  imports: [Charts, TitleCasePipe, MatSelect, MatOption],
  templateUrl: './father-charts.html',
  styleUrl: './father-charts.scss',
})
export class FatherCharts {


    private http = inject(HttpClient);
  list = ["distribution_by_category","distribution_by_risk_flag","time_series_by_date"];
   private route = inject(ActivatedRoute);
   analysisId = this.route.snapshot.queryParamMap.get('analysisId');
   typeMetric = signal<string>('');
 


  
  chartData = signal<any[]>([]);

 
  chartLabels = signal<string[]>([ ]);

  totalData :any = null;

analysisResource = rxResource<any,any>({
params: () => ({
  id:this.analysisId,
  graph:this.typeMetric(),
    
    }),

    stream: ({ params }) => {
      
       
          // this.defaultParams(params);
      

      return this.http.get<ApiResponse<any>>(`http://localhost:8080/call/analysis-metrics/${params.id}`)
      .pipe(
        tap((risposta:any)=>{ 
        
        
          this.totalData=risposta?.payload;
          
        }),
      );
      
    
    },
  });





private  defaultParams(param:any):void{
  if(!param.graph){
  param.graph="distribution_by_risk_flag";
  }
  if(!param?.id){  
    param.id='82f62c28-4721-4195-9f68-3749f6ce49df';
    
  }
  return ;
} 

takeDataCharts(){
  
  this.chartData.set(Object.values(this.totalData?.[this.typeMetric()]) || []);
  this.chartLabels.set(Object.keys(this.totalData?.[this.typeMetric()]) || []);
}

  
onSystemChange(arg0: any) {

this.typeMetric.set(arg0);

this.takeDataCharts();
}
}




