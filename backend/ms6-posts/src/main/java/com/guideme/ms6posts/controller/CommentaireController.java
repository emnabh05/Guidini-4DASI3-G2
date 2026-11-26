package com.guideme.ms6posts.controller;

import com.guideme.ms6posts.dto.CommentaireResponse;
import com.guideme.ms6posts.dto.CommentaireUpdateRequest;
import com.guideme.ms6posts.service.CommentaireService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
public class CommentaireController {

    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @GetMapping("/{id}")
    public CommentaireResponse parId(@PathVariable Long id) {
        return commentaireService.getCommentaire(id);
    }

    @GetMapping("/{id}/reponses")
    public List<CommentaireResponse> reponses(@PathVariable Long id) {
        return commentaireService.getReponses(id);
    }

    @PutMapping("/{id}")
    public CommentaireResponse modifier(@PathVariable Long id,
                                        @Valid @RequestBody CommentaireUpdateRequest req) {
        return commentaireService.modifierCommentaire(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        commentaireService.supprimerCommentaire(id);
        return ResponseEntity.noContent().build();
    }
}
