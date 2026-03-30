import { Component, inject, signal } from '@angular/core';
import { Charts } from '../../components/charts/charts';
import { rxResource } from '@angular/core/rxjs-interop';
import { HttpClient } from '@angular/common/http';
import { ApiResponse } from '../../data/ApiResponse';
import { tap } from 'rxjs';
import { MatSelect, MatOption, MatLabel, MatFormField } from '@angular/material/select';
import { ActivatedRoute } from '@angular/router';
import { ChartType } from 'chart.js';

@Component({
  selector: 'app-father-charts',
  standalone: true,
  imports: [Charts, MatSelect, MatOption, MatLabel, MatFormField],
  templateUrl: './father-charts.html',
  styleUrl: './father-charts.scss',
})
export class FatherCharts {
  private http = inject(HttpClient);
  list: Map<String[], ChartType> = new Map<String[], ChartType>([
    [['Categoria', 'distributionByCategory'], 'pie'],
    [['Rischio', 'distributionByRiskFlag'], 'bar'],
    [['Data', 'timeSeriesByDate'], 'line'],
  ]);
  private route = inject(ActivatedRoute);
  analysisId = this.route.snapshot.queryParamMap.get('analysisId');
  typeMetric = signal<string>('');

  chartType = signal<ChartType>('line');

  chartData = signal<any[]>([]);

  chartLabels = signal<string[]>([]);

  totalData: any = null;

  analysisResource = rxResource<any, any>({
    params: () => ({
      id: this.analysisId,
      graph: this.typeMetric(),
    }),

    stream: ({ params }) => {
      return this.http
        .get<ApiResponse<any>>(`http://localhost:8080/call/analysis-metrics/${params.id}`)
        .pipe(
          tap((risposta: any) => {
            this.totalData = risposta?.payload;
          }),
        );
    },
  });

  takeDataCharts() {
    this.chartData.set(Object.values(this.totalData?.[this.typeMetric()]) || []);
    this.chartLabels.set(Object.keys(this.totalData?.[this.typeMetric()]) || []);
  }

  onSystemChange(arg0: any) {
    this.typeMetric.set(arg0[0][1]);
    this.chartType.set(arg0[1]);
    this.takeDataCharts();
  }
}
