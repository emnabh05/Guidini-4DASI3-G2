package com.guideme.ms6posts.controller;

import com.guideme.ms6posts.dto.CommentaireRequest;
import com.guideme.ms6posts.dto.CommentaireResponse;
import com.guideme.ms6posts.dto.PostRequest;
import com.guideme.ms6posts.dto.PostResponse;
import com.guideme.ms6posts.dto.PostUpdateRequest;
import com.guideme.ms6posts.dto.StatutRequest;
import com.guideme.ms6posts.enums.StatutPost;
import com.guideme.ms6posts.service.CommentaireService;
import com.guideme.ms6posts.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentaireService commentaireService;

    public PostController(PostService postService, CommentaireService commentaireService) {
        this.postService = postService;
        this.commentaireService = commentaireService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> creer(@Valid @RequestBody PostRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.creerPost(req));
    }

    @GetMapping
    public List<PostResponse> lister(@RequestParam(required = false) StatutPost statut) {
        return postService.getPosts(statut);
    }

    @GetMapping("/auteur/{idAuteur}")
    public List<PostResponse> parAuteur(@PathVariable Long idAuteur) {
        return postService.getPostsAuteur(idAuteur);
    }

    @GetMapping("/{id}")
    public PostResponse parId(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/{id}")
    public PostResponse modifier(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest req) {
        return postService.modifierPost(id, req);
    }

    @PatchMapping("/{id}/statut")
    public PostResponse changerStatut(@PathVariable Long id, @Valid @RequestBody StatutRequest req) {
        return postService.changerStatut(id, req.statut());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        postService.supprimerPost(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- commentaires d'un post ----------

    @PostMapping("/{idPost}/commentaires")
    public ResponseEntity<CommentaireResponse> commenter(@PathVariable Long idPost,
                                                         @Valid @RequestBody CommentaireRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentaireService.ajouterCommentaire(idPost, req));
    }

    @GetMapping("/{idPost}/commentaires")
    public List<CommentaireResponse> commentaires(@PathVariable Long idPost) {
        return commentaireService.getCommentairesPost(idPost);
    }
}
