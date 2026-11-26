/** Modèles de l'API ms6-posts (http://localhost:8086). */

export type StatutPost = 'BROUILLON' | 'PUBLIE' | 'ARCHIVE';

export interface Post {
  idPost: number;
  idAuteur: number;
  titre: string;
  contenu: string;
  imageUrl: string | null;
  dateCreation: string;
  statut: StatutPost;
}

export interface PostRequest {
  idAuteur: number;
  titre: string;
  contenu: string;
  imageUrl?: string | null;
  statut?: StatutPost;
}

export interface PostUpdateRequest {
  titre: string;
  contenu: string;
  imageUrl?: string | null;
}

export interface Commentaire {
  idCommentaire: number;
  idPost: number;
  idAuteur: number;
  contenu: string;
  dateCreation: string;
  idCommentaireParent: number | null;
}

export interface CommentaireRequest {
  idAuteur: number;
  contenu: string;
  idCommentaireParent?: number | null;
}
