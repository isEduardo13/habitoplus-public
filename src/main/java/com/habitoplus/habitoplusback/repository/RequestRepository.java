package com.habitoplus.habitoplusback.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.model.Request;
import com.habitoplus.habitoplusback.model.RequestPK;

@Repository
public interface RequestRepository extends JpaRepository<Request, RequestPK>{
    List<Request> findByGroupIdGroupOrderByDateRequestDesc(Integer idGroup);

    @Query("SELECT rq FROM Request rq WHERE rq.group.idGroup = :idGroup")
    Page<Request> findRequestsByIdGroup(@Param("idGroup") Integer idGroup, Pageable pageable);
}
