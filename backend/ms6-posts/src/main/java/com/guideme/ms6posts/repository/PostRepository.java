package com.guideme.ms6posts.repository;

import com.guideme.ms6posts.entity.Post;
import com.guideme.ms6posts.enums.StatutPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByDateCreationDesc();

    List<Post> findByStatutOrderByDateCreationDesc(StatutPost statut);

    List<Post> findByIdAuteurOrderByDateCreationDesc(Long idAuteur);
}
