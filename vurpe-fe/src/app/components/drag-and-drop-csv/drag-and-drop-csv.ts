import { CommonModule } from '@angular/common';
import { Component, HostBinding, HostListener, input, model, output, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'drag-and-drop-csv',
  imports: [MatButton, CommonModule],
  templateUrl: './drag-and-drop-csv.html',
  styleUrl: './drag-and-drop-csv.scss',
})
export class DragAndDropCsv {
  fileProcessed = output<File>();
  currentFile = signal<File | null>(null);
  isDragOver = signal(false);
  isUploading = signal(false);
  statusMessage = model<string>('');
  valueChange = output<string>();

  constructor() {}

  @HostBinding('class.drag-over') get dragOverClass() {
    return this.isDragOver();
  }

  @HostListener('dragover', ['$event'])
  onDragOver(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.isDragOver.set(true);
  }

  @HostListener('dragleave', ['$event'])
  onDragLeave(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.isDragOver.set(false);
  }

  @HostListener('drop', ['$event'])
  onDrop(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.isDragOver.set(false);

    const file = evt.dataTransfer?.files?.[0];
    if (file) {
      this.processFile(file);
    }
  }

  private processFile(file: File) {
    this.currentFile.set(file);
    this.statusMessage.set('');

    const isCsv = file.name.toLowerCase().endsWith('.csv') || file.type === 'text/csv';

    if (!isCsv) {
      this.statusMessage.set('Errore - Solo file .csv accettati');
      this.currentFile.set(file);
      return;
    }

    this.startUpload(file);
  }

  private startUpload(file: File) {
    this.isUploading.set(true);
    this.statusMessage.set('Caricamento in corso...');
    setTimeout(() => {
      this.isUploading.set(false);
      this.currentFile.set(file);
      this.fileProcessed.emit(file);
      this.statusMessage.set('File caricato');
    }, 2500);
  }

  reset() {
    this.currentFile.set(null);
    this.statusMessage.set(' ');
    this.isUploading.set(false);
  }
}
