import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { TabConfig } from '../data/TabConfig';

@Injectable({
  providedIn: 'root',
})
export class MetricsService {
  private router = inject(Router);
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.url}/call`;
  public tabsConfig: TabConfig = {
    title: 'metrics',
    columns: [
      'id',
      'fileId',
      'distributionByCategory',
      'distributionByRiskFlag',
      'timeSeriesByDate',
    ],
    buttons: ['Elimina', 'Visualizza'],
    new: true,
  };

  public getDetails(el: any) {
    this.router.navigate(['/charts'], { queryParams: { analysisId: el.id } });
  }

  public async addRow(id: string) {
    const httpParams = { fileId: id };
    return await this.http.post(`${this.apiUrl}/save-analysis`, null, { params: httpParams });
  }

  public async delete(id: string) {
    const httpParams = { id: id };
    return await this.http.post(`${this.apiUrl}/analysis/delete`, null, { params: httpParams });
  }
}
