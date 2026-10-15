package com.habitoplus.habitoplusback.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.habitoplus.habitoplusback.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}
