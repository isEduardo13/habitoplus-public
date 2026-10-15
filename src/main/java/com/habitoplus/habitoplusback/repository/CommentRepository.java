package com.habitoplus.habitoplusback.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Comment;
import com.habitoplus.habitoplusback.model.CommentPK;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentPK>{
    
    List<Comment> findByGroupIdGroup(Integer idGroup);

    Page<Comment> findByGroupIdGroupOrderByDateTimeDesc(Integer idGroup,  Pageable pageable);
    
    List<Comment> findByGroupIdGroupOrderByDateTimeDesc(Integer idGroup);
}

