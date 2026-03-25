import { Directive, input, computed } from '@angular/core';

@Directive({
  selector: '[appStatusBadge]',
  standalone: true,
  exportAs: 'statusBadge',
  host: {
    // Applichiamo gli stili solo se badgeConfig non è null
    '[style.background-color]': 'badgeConfig()?.bg || "transparent"',
    '[style.color]': 'badgeConfig()?.text || "inherit"',
    '[style.border-color]': 'badgeConfig()?.border || "transparent"',
    '[style.border-style]': 'badgeConfig() ? "solid" : "none"',
    '[style.display]': 'badgeConfig() ? "inline-block" : "inline"',
    '[style.padding]': 'badgeConfig() ? "2px 10px" : "0"',
    '[style.border-radius]': 'badgeConfig() ? "12px" : "0"',
    '[style.border-width]': 'badgeConfig() ? "1px" : "0"',
    '[style.font-size]': 'badgeConfig() ? "12px" : "inherit"',
    '[style.font-weight]': 'badgeConfig() ? "600" : "inherit"',
    '[style.text-transform]': 'badgeConfig() ? "uppercase" : "none"',
  },
})
export class StatusBadgeDirective {
  status = input<any>(null, { alias: 'appStatusBadge' });

  badgeConfig = computed(() => {
    let val = this.status();

    // 1. Se è null, undefined o una stringa vuota, salta.
    if (val === null || val === undefined || val === '') val = 'UNKNOWN';

    // 2. Se è un numero puro (es. 500) o una stringa che contiene solo numeri (es. "500"), salta.
    const isNumeric = !isNaN(val) && !isNaN(parseFloat(val));
    if (isNumeric) return null;

    // 3. Altrimenti procediamo con le stringhe di stato
    const s = String(val).toUpperCase().trim() ?? 'UNKNOWN';

    const configs: Record<string, { bg: string; text: string; border: string; label: string }> = {
      LOW: { bg: '#e8f5e9', text: '#2e7d32', border: '#c8e6c9', label: 'BASSO' },
      MEDIUM: { bg: '#fff3e0', text: '#ef6c00', border: '#ffe0b2', label: 'MEDIO' },
      HIGH: { bg: '#ffebee', text: '#e32222', border: '#ffcdd2', label: 'ALTO' },
      CRITICAL: { bg: '#ffebee', text: '#c00a0a', border: '#ffcdd2', label: 'CRITICO' },
      COMPLETED: { bg: '#e8f5e9', text: '#2e7d32', border: '#c8e6c9', label: 'COMPLETATO' },
      FAILED: { bg: '#ffebee', text: '#c62828', border: '#ffcdd2', label: 'FALLITO' },
      ERROR: { bg: '#fff3e0', text: '#ef6c00', border: '#ffe0b2', label: 'ERRORE' },
      QUEUED: { bg: '#e3f2fd', text: '#1565c0', border: '#bbdefb', label: 'IN CODA' },
      PROCESSING: { bg: '#f3e5f5', text: '#7b1fa2', border: '#e1bee7', label: 'IN ANALISI' },
      UNKNOWN: { bg: '#f5f5f5', text: '#757575', border: '#e0e0e0', label: 'DA ANALIZZARE' },
      UPLOADED: { bg: '#f5f5f5', text: '#757575', border: '#e0e0e0', label: 'CARICATO' },
    };

    // Restituisce la config se esiste, altrimenti null
    return configs[s] ?? configs['UNKNOWN'];
  });

  get label() {
    return this.badgeConfig()?.label || null;
  }
}
