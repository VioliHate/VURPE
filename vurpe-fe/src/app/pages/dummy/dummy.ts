import { Component, signal } from '@angular/core';
import { DynamicTable } from '../../components/dynamic-table/dynamic-table';

@Component({
  selector: 'app-dummy',
  imports: [DynamicTable],
  template: `

  `,

  styleUrl: './dummy.scss',
})
export class Dummy {
  users = signal([
    { id: 1, nome: 'Mario Rossi', email: 'mario@email.it', ruolo: 'Admin' },
    { id: 2, nome: 'Luigi Verdi', email: 'luigi@email.it', ruolo: 'User' },
    { id: 3, nome: 'Giulia Neri', email: 'giulia@email.it', ruolo: 'Editor' },
  ]);

  products = signal([
    { id: 101, nome: 'Laptop', prezzo: 1200, stock: 5 },
    { id: 102, nome: 'Smartphone', prezzo: 800, stock: 12 },
  ]);
}
