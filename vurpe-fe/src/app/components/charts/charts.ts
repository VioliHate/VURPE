import { Component, computed, input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-charts',
  standalone: true,
  imports: [BaseChartDirective, FormsModule],
  templateUrl: './charts.html',
  styleUrl: './charts.scss',
})
export class Charts {
  systemName = input<string>('MF1');
  chartData = input.required<any[]>();
  chartLabels = input.required<string[]>();
  chartType = input<ChartType>('line');

  palette = [
    '#3b82f6', // Blue
    '#10b981', // Emerald
    '#6366f1', // Indigo
    '#f59e0b', // Amber
    '#ec4899', // Pink
    '#8b5cf6', // Violet
    '#94a3b8', // Slate (per i dati "altri")
  ];

  public lineChartData = computed<ChartData>(() => {
    const isPie = this.chartType() === 'pie' || this.chartType() === 'doughnut';
    return {
      labels: this.chartLabels(),
      datasets: [
        {
          data: this.chartData(),
          label: this.systemName(),
          backgroundColor: isPie ? this.palette : 'rgba(54, 162, 235, 0.2)',
          borderColor: isPie ? '#ffffff' : '#2563eb',
          borderWidth: isPie ? 1 : 2,
          pointBackgroundColor: '#2563eb',
          fill: this.chartType() === 'line',
          tension: 0.4,
        },
      ],
    };
  });

  public lineChartOptions = computed<ChartConfiguration['options']>(() => {
    const isPie = this.chartType() === 'pie' || this.chartType() === 'doughnut';

    return {
      responsive: true,
      maintainAspectRatio: isPie,
      aspectRatio: isPie ? 2 : undefined,

      plugins: {
        legend: {
          display: true,
          position: isPie ? 'right' : 'top',
        },
        tooltip: {
          backgroundColor: 'rgba(0,0,0,0.8)',
          padding: 10,
        },
      },
      scales: isPie
        ? {}
        : {
            y: {
              beginAtZero: true,
              grid: { color: '#f1f5f9' },
            },
            x: {
              grid: { display: false },
            },
          },
    };
  });

  public chartClicked(e: any): void {
    console.log(e);
  }
  public chartHovered(e: any): void {
    console.log(e);
  }
}
