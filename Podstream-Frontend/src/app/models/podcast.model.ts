export interface Podcast {
  trackName: string;
  artistName: string;
  artworkUrl100: string;
  collectionName: string;
  feedUrl: string;
  episodes?: Episode[];
}

export interface Episode {
  title: string;
  audioUrl?: string;
  pubDate?: string;
  description?: string;
}