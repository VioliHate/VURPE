import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import {
  Component,
  EventEmitter,
  HostBinding,
  HostListener,
  Input,
  Output,
  signal,
} from '@angular/core';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'drag-and-drop-csv',
  imports: [MatButton, CommonModule],
  templateUrl: './drag-and-drop-csv.html',
  styleUrl: './drag-and-drop-csv.scss',
})
export class DragAndDropCsv {
  @Input() maxSizeMB = 10;
  @Input() uploadUrl = '/api/upload-csv';
  @Input() httpMethod: 'POST' | 'PUT' = 'POST';

  @Output() fileProcessed = new EventEmitter<File>();
  @Output() uploadSuccess = new EventEmitter<any>();
  @Output() uploadError = new EventEmitter<Error | any>();

  currentFile = signal<File | null>(null);
  isDragOver = signal(false);
  isUploading = signal(false);
  statusMessage = signal<string>('');

  constructor(private http: HttpClient) {}

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
    // Sostituisci file precedente (uno alla volta)
    this.currentFile.set(file);
    this.statusMessage.set('');

    const isCsv = file.name.toLowerCase().endsWith('.csv') || file.type === 'text/csv';
    const isTooBig = file.size > this.maxSizeMB * 1024 * 1024;

    if (!isCsv) {
      this.statusMessage.set('Solo file .csv accettati');
      this.currentFile.set(null);
      return;
    }

    if (isTooBig) {
      this.statusMessage.set(`Il file supera i ${this.maxSizeMB} MB`);
      this.currentFile.set(null);
      return;
    }

    this.fileProcessed.emit(file);
    this.startUpload(file);
  }

  private startUpload(file: File) {
    this.isUploading.set(true);
    this.statusMessage.set('Caricamento in corso...');

    const formData = new FormData();
    formData.append('file', file);

    this.http[this.httpMethod.toLowerCase() as 'post' | 'put'](this.uploadUrl, formData).subscribe({
      next: (response) => {
        this.isUploading.set(false);
        this.statusMessage.set('Caricamento completato con successo!');
        this.uploadSuccess.emit(response);
        setTimeout(() => this.reset(), 2500);
      },
      error: (err) => {
        this.isUploading.set(false);
        this.statusMessage.set('Errore durante il caricamento');
        this.uploadError.emit(err);
      },
    });
  }

  reset() {
    this.currentFile.set(null);
    this.statusMessage.set('');
    this.isUploading.set(false);
  }
}
