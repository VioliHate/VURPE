import { Directive, input, computed } from '@angular/core';

@Directive({
  selector: '[appStatusBadge]',
  standalone: true,
  // Usiamo host per applicare stili e classi direttamente all'elemento
  host: {
    '[style.background-color]': 'badgeConfig().bg',
    '[style.color]': 'badgeConfig().text',
    '[style.border-color]': 'badgeConfig().border',
    '[style.display]': '"inline-block"',
    '[style.padding]': '"2px 10px"',
    '[style.border-radius]': '"12px"',
    '[style.border-width]': '"1px"',
    '[style.border-style]': '"solid"',
    '[style.font-size]': '"12px"',
    '[style.font-weight]': '"600"',
    '[style.text-transform]': '"uppercase"',
  }
})
export class StatusBadgeDirective {
  // Signal Input: Riceve lo stato (es. 'FAILED')
  // L'alias permette di usare [appStatusBadge]="valore"
  status = input<string | null>(null, { alias: 'appStatusBadge' });

  // Computed Signal: si aggiorna automaticamente solo quando cambia lo status
  badgeConfig = computed(() => {
    const s = this.status()?.toUpperCase() ?? 'UNKNOWN';

    const configs: Record<string, { bg: string; text: string; border: string }> = {
      'COMPLETED': { bg: '#e8f5e9', text: '#2e7d32', border: '#c8e6c9' },
      'LOW':       { bg: '#e8f5e9', text: '#2e7d32', border: '#c8e6c9' },
      'FAILED':    { bg: '#ffebee', text: '#c62828', border: '#ffcdd2' },
      'CRITICAL':  { bg: '#ffebee', text: '#c62828', border: '#ffcdd2' },
      'HIGH':      { bg: '#ffebee', text: '#c62828', border: '#ffcdd2' },
      'ERROR':     { bg: '#fff3e0', text: '#ef6c00', border: '#ffe0b2' },
      'MEDIUM':    { bg: '#fff3e0', text: '#ef6c00', border: '#ffe0b2' },
      'QUEUED':    { bg: '#e3f2fd', text: '#1565c0', border: '#bbdefb' },
      'PROCESSING':{ bg: '#f3e5f5', text: '#7b1fa2', border: '#e1bee7' },
      'UNKNOWN':   { bg: '#f5f5f5', text: '#757575', border: '#e0e0e0' }
    };

    return configs[s] ?? configs['UNKNOWN'];
  });
}