import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class MetricsService {
   private router = inject(Router);
   
    public getDetails(id: string) {
    this.router.navigate(['/charts'], { queryParams: { analysisId: id } });
  }
}
