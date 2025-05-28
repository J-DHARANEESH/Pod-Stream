import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Episode, Podcast } from '../models/podcast.model';

@Injectable({ providedIn: 'root' })
export class PodcastService {
  private readonly API_URL = 'http://localhost:8080/api/search';

  constructor(private http: HttpClient) {}

  search(term: string): Observable<Podcast[]> {
    const params = new HttpParams().set('term', term.trim());

    return this.http.get<Podcast[]>(this.API_URL, { params });
  }

  getEpisodes(feedUrl: string): Observable<Episode[]> {
  return this.http.get<Episode[]>('http://localhost:8080/api/episodes', {
    params: new HttpParams().set('feedUrl', feedUrl)
  });
}

}
