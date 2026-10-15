package com.habitoplus.habitoplusback.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.GroupMemberDTO;
import com.habitoplus.habitoplusback.enums.Role;
import com.habitoplus.habitoplusback.exception.GroupEmptyException;
import com.habitoplus.habitoplusback.exception.UserAlreadyExistsException;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.GroupMember;
import com.habitoplus.habitoplusback.model.GroupMemberPK;
import com.habitoplus.habitoplusback.repository.GroupMemberRepository;
import com.habitoplus.habitoplusback.security.authority.GroupAuthority;

import jakarta.transaction.Transactional;

@Service
public class GroupMemberService {
	@Autowired
	private GroupMemberRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	public List<GroupMember> getAll(Integer idGroup) {
		return repository.findByGroupIdGroup(idGroup);
	}

	public List<GroupMember> getAll(Integer idGroup, int page, int pageSize) {
		PageRequest pageReq = PageRequest.of(page, pageSize);
		Page<GroupMember> members = repository.findMembersByIdGroup(idGroup, pageReq);
		return members.getContent();
	}

	public GroupMember getById(Integer idGroup, Integer idProfile) {
		return repository.findById(new GroupMemberPK(idGroup, idProfile)).orElseThrow(
				() -> new NoSuchElementException());
	}

	@Transactional
	public GroupMember save(GroupMemberDTO memberDTO) {
		if (repository.findById(new GroupMemberPK(memberDTO.getIdGroup(), memberDTO.getIdProfile())).isPresent()) {
			throw new UserAlreadyExistsException("This user already belongs to this group.");
		}
		GroupMember member = convertToEntity(memberDTO);
		member.setRole(Role.ROLE_MEMBER);
		member.setUnionDate(new Date());

		return repository.save(member);
	}
	
 public Set<GrantedAuthority> getGroupAuthorities(Account account) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        if (account == null || account.getProfile() == null) {
            return authorities;
        }

        List<GroupMember> memberships = repository.findByProfile(account.getProfile());
        
        
        for (GroupMember member : memberships) {
			authorities.add(new GroupAuthority(member.getGroup().getIdGroup(), member.getRole(), authorities));
        }
        
        return authorities;
    }
	
	

	@Transactional
	public void delete(Integer idGroup, Integer idProfile) {
		if (repository.findById(new GroupMemberPK(idGroup, idProfile)).isEmpty()) {
			throw new NoSuchElementException();
		}

		if (repository.existsByGroupIdGroupAndProfileIdProfileAndRole(idGroup, idProfile, Role.ROLE_GROUPADMIN)) {
			if (repository.findFirstNonAdminByGroupIdGroupOrderByUnionDateAsc(idGroup).isEmpty()) {
				throw new GroupEmptyException("This group cannot be left empty");
			}
			GroupMember oldestMember = repository.findFirstNonAdminByGroupIdGroupOrderByUnionDateAsc(idGroup).get(0);
			oldestMember.setRole(Role.ROLE_GROUPADMIN);
			repository.save(oldestMember);
		}

		repository.deleteById(new GroupMemberPK(idGroup, idProfile));
	}

	public GroupMember convertToEntity(GroupMemberDTO memberDTO) {
		return modelMapper.map(memberDTO, GroupMember.class);
	}

	public GroupMemberDTO convertToDTO(GroupMember member) {
		return modelMapper.map(member, GroupMemberDTO.class);
	}
}
