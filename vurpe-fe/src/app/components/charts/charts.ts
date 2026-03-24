
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
  // --- SIGNAL INPUTS (Sostituiscono @Input) ---
  systemName = input<string>("MF1");
  chartData = input.required<any[]>();
  chartLabels = input.required<string[]>();
  chartType = input<ChartType>('line');

  public lineChartData = computed<ChartData>(() => {
    return {
      labels: this.chartLabels(),
      datasets: [
        {
          data: this.chartData(),
          label: this.systemName(),
         
        
          pointBackgroundColor: 'red',
          pointBorderColor: '#fff',
        
             backgroundColor: this.chartType() === 'pie' || this.chartType() === 'doughnut' 
        ? ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'] // Colori diversi per le fette
        : '#36A2EB', // Colore unico per le barre/linee
      borderColor: '#36A2EB',
      fill: this.chartType() === 'line', // Riempimento solo per il grafico a linee
        }
      ]
    };
  });



  public lineChartOptions: any = {
responsive: true,
scales : {
yAxes: [{
ticks: {
max : 60,
min : 0,
}
}],
xAxes: [{

}],
},
  plugins: {
  datalabels: {
    display: true,
    align: 'top',
    anchor: 'end',
    //color: "#2756B3",
    color: "#222",

    font: {
      family: 'FontAwesome',
      size: 14
    },
  
  },
  deferred: false

},
};
_lineChartColors:Array<any> = [{
backgroundColor: 'red',
borderColor: 'red',
pointBackgroundColor: 'red',
pointBorderColor: 'red',
pointHoverBackgroundColor: 'red',
pointHoverBorderColor: 'red'
}];
public chartClicked(e: any): void {
console.log(e);
}
public chartHovered(e: any): void {
console.log(e);
}
}