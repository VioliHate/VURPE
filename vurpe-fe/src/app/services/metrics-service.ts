import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { TabConfig } from '../entities/TabConfig';

@Injectable({
  providedIn: 'root',
})
export class MetricsService {
   private router = inject(Router);
   private http = inject(HttpClient);
   private readonly apiUrl = `${environment.url}/call`;
   public tabsConfig: TabConfig = 
    {
      columns: ['id','file_id','distribution_by_category','distribution_by_risk_flag','time_series_by_date'],
      buttons: ['Elimina','Visualizza'],
      new:true
    };
  
   
    public getDetails(id: string) {
    this.router.navigate(['/charts'], { queryParams: { analysisId: id } });
  }

  public async addRow(id: string) {
    console.log('clicked add new row with id:', id);
    const httpParams = { fileId: id };
return await this.http.post(`${this.apiUrl}/save-analysis`,null, {params: httpParams} );
  }
}
