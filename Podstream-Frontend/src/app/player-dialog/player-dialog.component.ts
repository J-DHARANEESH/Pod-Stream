import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Episode } from '../models/podcast.model';

@Component({
  standalone:false,
  selector: 'app-player-dialog',
  templateUrl: './player-dialog.component.html',
  styleUrls: ['./player-dialog.component.scss']
})
export class PlayerDialogComponent {
  episodes: Episode[];
  currentIndex: number;

  constructor(
    public dialogRef: MatDialogRef<PlayerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { episodes: Episode[], index: number }
  ) {
    this.episodes = data.episodes;
    this.currentIndex = data.index;
  }

  get currentEpisode(): Episode {
    return this.episodes[this.currentIndex];
  }

  previous() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  next() {
    if (this.currentIndex < this.episodes.length - 1) {
      this.currentIndex++;
    }
  }

  close() {
    this.dialogRef.close();
  }
}
