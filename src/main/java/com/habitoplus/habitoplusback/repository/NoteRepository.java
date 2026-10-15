package com.habitoplus.habitoplusback.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    
    // bUSCAr notification por ID del Perfil 
    @Query("SELECT n FROM Note n WHERE n.profile.idProfile = :profileId")
    Page<Note> findByProfileId(@Param ("profileId") Integer profileId , Pageable pageable);


}
