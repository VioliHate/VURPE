import { inject, Injectable } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { Location } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class NavigationService {
  private history: string[] = [];
  private router = inject(Router);
  private location = inject(Location);
  
  constructor() { 
    this.router.events.pipe(filter(event=> event instanceof NavigationEnd))
    .subscribe((event: any) => {
      const url = event.urlAfterRedirects;
       if(this.history[this.history.length - 1] !== url) {
        this.history.push(url);
      }
      console.log('Navigation history:', this.history);
    });
}
public back() :void{
  this.history.pop(); // Rimuove la pagina corrente
  if(this.history.length > 0) {
    console.log("torno indietro via location");
    this.location.back(); // Torna alla pagina precedente
  }
  else {
    console.log("torno indietro via router");
    this.router.navigateByUrl('/'); // fallback sicuro dentro l'app
  }
}
}

