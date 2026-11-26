package com.guideme.ms6posts.repository;

import com.guideme.ms6posts.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    List<Commentaire> findByIdPostOrderByDateCreationAsc(Long idPost);

    List<Commentaire> findByIdCommentaireParent(Long idCommentaireParent);

    List<Commentaire> findByIdPost(Long idPost);
}
