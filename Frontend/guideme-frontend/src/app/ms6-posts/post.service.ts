import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Commentaire, CommentaireRequest, Post, PostRequest, PostUpdateRequest, StatutPost } from './post.model';

/** Adresse du microservice ms6-posts. */
export const MS6_API = 'http://localhost:8086/api';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly http = inject(HttpClient);

  // ---------- posts ----------
  getPosts(statut?: StatutPost | null): Observable<Post[]> {
    const params = statut ? new HttpParams().set('statut', statut) : undefined;
    return this.http.get<Post[]>(`${MS6_API}/posts`, { params });
  }

  getPostsAuteur(idAuteur: number): Observable<Post[]> {
    return this.http.get<Post[]>(`${MS6_API}/posts/auteur/${idAuteur}`);
  }

  getPost(id: number): Observable<Post> {
    return this.http.get<Post>(`${MS6_API}/posts/${id}`);
  }

  creerPost(req: PostRequest): Observable<Post> {
    return this.http.post<Post>(`${MS6_API}/posts`, req);
  }

  modifierPost(id: number, req: PostUpdateRequest): Observable<Post> {
    return this.http.put<Post>(`${MS6_API}/posts/${id}`, req);
  }

  changerStatut(id: number, statut: StatutPost): Observable<Post> {
    return this.http.patch<Post>(`${MS6_API}/posts/${id}/statut`, { statut });
  }

  supprimerPost(id: number): Observable<void> {
    return this.http.delete<void>(`${MS6_API}/posts/${id}`);
  }

  // ---------- commentaires ----------
  getCommentaires(idPost: number): Observable<Commentaire[]> {
    return this.http.get<Commentaire[]>(`${MS6_API}/posts/${idPost}/commentaires`);
  }

  commenter(idPost: number, req: CommentaireRequest): Observable<Commentaire> {
    return this.http.post<Commentaire>(`${MS6_API}/posts/${idPost}/commentaires`, req);
  }

  modifierCommentaire(id: number, contenu: string): Observable<Commentaire> {
    return this.http.put<Commentaire>(`${MS6_API}/commentaires/${id}`, { contenu });
  }

  supprimerCommentaire(id: number): Observable<void> {
    return this.http.delete<void>(`${MS6_API}/commentaires/${id}`);
  }
}

/** Transforme une erreur HTTP en message lisible (format ErrorResponse du backend). */
export function messageErreur(err: unknown): string {
  if (err instanceof HttpErrorResponse) {
    if (err.status === 0) return 'Le service Publications (ms6, port 8086) est injoignable.';
    return err.error?.message ?? `Erreur ${err.status}`;
  }
  return 'Erreur inattendue';
}
