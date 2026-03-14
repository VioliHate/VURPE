import { inject, Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  public getDetails(id: string) {
    console.log('id:' + id);
    this.router.navigate(['/dataRecords'], { queryParams: { file_id: id } });
  }
}
