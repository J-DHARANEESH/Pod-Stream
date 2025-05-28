import { Component } from '@angular/core';
import { PodcastService } from '../../services/podcast.service';
import { Podcast } from '../../models/podcast.model';

@Component({
  standalone: false,
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  searchTerm = '';
  podcasts: Podcast[] = [];
  hasSearched = false;
  isLoading =false;
errorMessage: any;

  constructor(private podcastService: PodcastService) {}

 onSearch(): void {
  if (!this.searchTerm.trim()) return;

  this.isLoading = true;
  this.errorMessage = null;

  this.podcastService.search(this.searchTerm).subscribe({
    next: data => {
      this.podcasts = data;
      this.hasSearched = true;
      this.isLoading = false;
    },
    error: err => {
      console.error('Error fetching podcasts:', err);
      this.isLoading = false;
      this.errorMessage = 'Oops! Something went wrong. Please try again later.';
      this.hasSearched = false; 
    }
  });
}

}
