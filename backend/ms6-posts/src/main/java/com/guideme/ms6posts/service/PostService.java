package com.guideme.ms6posts.service;

import com.guideme.ms6posts.dto.PostRequest;
import com.guideme.ms6posts.dto.PostResponse;
import com.guideme.ms6posts.dto.PostUpdateRequest;
import com.guideme.ms6posts.entity.Post;
import com.guideme.ms6posts.enums.StatutPost;
import com.guideme.ms6posts.exception.ConflictException;
import com.guideme.ms6posts.exception.ResourceNotFoundException;
import com.guideme.ms6posts.repository.CommentaireRepository;
import com.guideme.ms6posts.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentaireRepository commentaireRepository;

    public PostService(PostRepository postRepository, CommentaireRepository commentaireRepository) {
        this.postRepository = postRepository;
        this.commentaireRepository = commentaireRepository;
    }

    @Transactional
    public PostResponse creerPost(PostRequest req) {
        Post post = new Post();
        post.setIdAuteur(req.idAuteur());
        post.setTitre(req.titre());
        post.setContenu(req.contenu());
        post.setImageUrl(req.imageUrl());
        post.setStatut(req.statut() != null ? req.statut() : StatutPost.PUBLIE);
        post.setDateCreation(LocalDateTime.now());
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(StatutPost statut) {
        List<Post> posts = (statut == null)
                ? postRepository.findAllByOrderByDateCreationDesc()
                : postRepository.findByStatutOrderByDateCreationDesc(statut);
        return posts.stream().map(PostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsAuteur(Long idAuteur) {
        return postRepository.findByIdAuteurOrderByDateCreationDesc(idAuteur)
                .stream().map(PostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        return PostResponse.from(trouver(id));
    }

    @Transactional
    public PostResponse modifierPost(Long id, PostUpdateRequest req) {
        Post post = trouver(id);
        if (post.getStatut() == StatutPost.ARCHIVE) {
            throw new ConflictException("Impossible de modifier un post ARCHIVE");
        }
        post.setTitre(req.titre());
        post.setContenu(req.contenu());
        post.setImageUrl(req.imageUrl());
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public PostResponse changerStatut(Long id, StatutPost statut) {
        Post post = trouver(id);
        if (post.getStatut() == statut) {
            throw new ConflictException("Le post est déjà " + statut);
        }
        post.setStatut(statut);
        return PostResponse.from(postRepository.save(post));
    }

    /** Supprime le post et tous ses commentaires. */
    @Transactional
    public void supprimerPost(Long id) {
        Post post = trouver(id);
        commentaireRepository.deleteAll(commentaireRepository.findByIdPost(id));
        postRepository.delete(post);
    }

    Post trouver(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " introuvable"));
    }
}
