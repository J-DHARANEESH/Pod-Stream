import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { Episode } from '../models/podcast.model';
import { PlayerDialogComponent } from '../player-dialog/player-dialog.component';

@Component({
  standalone:false,
  selector: 'app-episode-dialog',
  templateUrl: './episode-dialog.component.html',
  styleUrls: ['./episode-dialog.component.scss']
})
export class EpisodeDialogComponent {
  episodes: Episode[];

  constructor(
    public dialogRef: MatDialogRef<EpisodeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { episodes: Episode[] },
    private dialog: MatDialog
  ) {
    this.episodes = data.episodes;
  }

  openPlayerDialog(index: number) {
    this.dialogRef.close();
    this.dialog.open(PlayerDialogComponent, {
      width: '500px',
      data: {
        episodes: this.episodes,
        index: index
      }
    });
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
