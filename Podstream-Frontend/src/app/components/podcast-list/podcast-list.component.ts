import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Podcast } from '../../models/podcast.model';
import { EpisodeDialogComponent } from '../../episode-dialog/episode-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  standalone:false,
  selector: 'app-podcast-list',
  templateUrl: './podcast-list.component.html',
  styleUrls: ['./podcast-list.component.scss']
})
export class PodcastListComponent {
  @Input() podcasts: Podcast[] = [];

  constructor(private dialog: MatDialog, private snackBar: MatSnackBar) {}

  openEpisodesDialog(podcast: Podcast) {
  if (!podcast.episodes || podcast.episodes.length === 0) {
    this.snackBar.open('No episodes available for this podcast.', 'Close', {
      duration: 3000, 
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: ['snack-bar-warning']
    });
    return;
  }

    this.dialog.open(EpisodeDialogComponent, {
      data: { episodes: podcast.episodes }
    });
  }
}
