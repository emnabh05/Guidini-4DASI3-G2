package com.guideme.ms6posts.service;

import com.guideme.ms6posts.dto.CommentaireRequest;
import com.guideme.ms6posts.dto.CommentaireResponse;
import com.guideme.ms6posts.dto.CommentaireUpdateRequest;
import com.guideme.ms6posts.entity.Commentaire;
import com.guideme.ms6posts.entity.Post;
import com.guideme.ms6posts.enums.StatutPost;
import com.guideme.ms6posts.exception.BadRequestException;
import com.guideme.ms6posts.exception.ConflictException;
import com.guideme.ms6posts.exception.ResourceNotFoundException;
import com.guideme.ms6posts.repository.CommentaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final PostService postService;

    public CommentaireService(CommentaireRepository commentaireRepository, PostService postService) {
        this.commentaireRepository = commentaireRepository;
        this.postService = postService;
    }

    @Transactional
    public CommentaireResponse ajouterCommentaire(Long idPost, CommentaireRequest req) {
        Post post = postService.trouver(idPost);
        if (post.getStatut() != StatutPost.PUBLIE) {
            throw new ConflictException("Impossible de commenter un post " + post.getStatut());
        }
        if (req.idCommentaireParent() != null) {
            Commentaire parent = trouver(req.idCommentaireParent());
            if (!parent.getIdPost().equals(idPost)) {
                throw new BadRequestException("Le commentaire parent " + parent.getIdCommentaire()
                        + " n'appartient pas au post " + idPost);
            }
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setIdPost(idPost);
        commentaire.setIdAuteur(req.idAuteur());
        commentaire.setContenu(req.contenu());
        commentaire.setIdCommentaireParent(req.idCommentaireParent());
        commentaire.setDateCreation(LocalDateTime.now());
        return CommentaireResponse.from(commentaireRepository.save(commentaire));
    }

    @Transactional(readOnly = true)
    public List<CommentaireResponse> getCommentairesPost(Long idPost) {
        postService.trouver(idPost);
        return commentaireRepository.findByIdPostOrderByDateCreationAsc(idPost)
                .stream().map(CommentaireResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CommentaireResponse> getReponses(Long idCommentaire) {
        trouver(idCommentaire);
        return commentaireRepository.findByIdCommentaireParent(idCommentaire)
                .stream().map(CommentaireResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CommentaireResponse getCommentaire(Long id) {
        return CommentaireResponse.from(trouver(id));
    }

    @Transactional
    public CommentaireResponse modifierCommentaire(Long id, CommentaireUpdateRequest req) {
        Commentaire commentaire = trouver(id);
        commentaire.setContenu(req.contenu());
        return CommentaireResponse.from(commentaireRepository.save(commentaire));
    }

    /** Supprime le commentaire et toutes ses réponses, à tous les niveaux. */
    @Transactional
    public void supprimerCommentaire(Long id) {
        Commentaire racine = trouver(id);
        List<Commentaire> aSupprimer = new ArrayList<>();
        Deque<Commentaire> aParcourir = new ArrayDeque<>();
        aParcourir.push(racine);
        while (!aParcourir.isEmpty()) {
            Commentaire courant = aParcourir.pop();
            aSupprimer.add(courant);
            commentaireRepository.findByIdCommentaireParent(courant.getIdCommentaire())
                    .forEach(aParcourir::push);
        }
        commentaireRepository.deleteAll(aSupprimer);
    }

    Commentaire trouver(Long id) {
        return commentaireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire " + id + " introuvable"));
    }
}
