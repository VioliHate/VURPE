import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'camelToTitle',
  standalone: true // Indica che è una pipe standalone (Angular 14+)
})
export class CamelToTitlePipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';

    // 1. Separa il camelCase (es. "createdAt" -> "created At")
    // 2. Sostituisce eventuali underscore con spazi (es. "created_at" -> "created at")
    const spacedString = value
      .replace(/([a-z])([A-Z])/g, '$1 $2')
      .replace(/_/g, ' ');

    // 3. Rende maiuscola la prima lettera di ogni parola
    return spacedString
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }
}
