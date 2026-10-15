package com.habitoplus.habitoplusback.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.habitoplus.habitoplusback.enums.Role;
import com.habitoplus.habitoplusback.model.Group;
import com.habitoplus.habitoplusback.model.GroupMember;
import com.habitoplus.habitoplusback.model.GroupMemberPK;
import com.habitoplus.habitoplusback.model.Profile;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberPK> {

    boolean existsByGroupIdGroupAndProfileIdProfileAndRole(Integer idGroup, Integer idProfile, Role role);

    @Query("SELECT gm FROM GroupMember gm WHERE gm.group.idGroup = :idGroup AND gm.role <> 'ADMIN' ORDER BY gm.unionDate ASC")
    List<GroupMember> findFirstNonAdminByGroupIdGroupOrderByUnionDateAsc(@Param("idGroup") Integer idGroup);

    @Query("SELECT gm FROM GroupMember gm WHERE gm.group.idGroup = :idGroup")
    Page<GroupMember> findMembersByIdGroup(@Param("idGroup") Integer idGroup, PageRequest pageRequest);

    @Query("SELECT CASE WHEN COUNT(gm) > 0 THEN TRUE ELSE FALSE END FROM GroupMember gm WHERE gm.group.idGroup = :idGroup")
    boolean existsGroupMembersByGroupId(@Param("idGroup") Integer idGroup);

    List<GroupMember> findByGroupIdGroup(Integer idGroup);

    List<GroupMember> findByProfile(Profile profile);

    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.profile.idProfile = :idProfile")
    Page<Group> findGroupsByProfileId(@Param("idProfile") Integer idProfile, PageRequest pageRequest);

}
