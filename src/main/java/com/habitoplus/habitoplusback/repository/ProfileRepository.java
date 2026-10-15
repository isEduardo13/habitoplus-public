package com.habitoplus.habitoplusback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {


}
