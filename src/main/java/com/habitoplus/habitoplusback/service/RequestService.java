package com.habitoplus.habitoplusback.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.GroupMemberDTO;
import com.habitoplus.habitoplusback.dto.RequestDTO;
import com.habitoplus.habitoplusback.enums.Status;
import com.habitoplus.habitoplusback.exception.UserAlreadyExistsException;
import com.habitoplus.habitoplusback.model.Request;
import com.habitoplus.habitoplusback.model.RequestPK;
import com.habitoplus.habitoplusback.repository.RequestRepository;

import jakarta.transaction.Transactional;

@Service
public class RequestService {
	@Autowired
	private RequestRepository repository;

	@Autowired
	private GroupMemberService memberService;

	@Autowired
	private ModelMapper modelMapper;

	public List<Request> getAll(Integer idGroup) {
		return repository.findByGroupIdGroupOrderByDateRequestDesc(idGroup);
	}

	public List<Request> getAll(Integer idGroup, int page, int pageSize) {
		PageRequest pageReq = PageRequest.of(page, pageSize);
		Page<Request> requests = repository.findRequestsByIdGroup(idGroup, pageReq);
		return requests.getContent();
	}

	public Request getById(Integer idGroup, Integer idProfile) {
		return repository.findById(new RequestPK(idGroup, idProfile)).orElseThrow(
				() -> new NoSuchElementException());
	}

	@Transactional
	public Request save(RequestDTO requestDTO) {
		if (repository.findById(new RequestPK(requestDTO.getIdGroup(), requestDTO.getIdProfile())).isPresent()) {
			throw new UserAlreadyExistsException("This user has already sent a request to this group");
		}
		Request request = convertToEntity(requestDTO);
		request.setStatus(Status.SENT);
		request.setDateRequest(new Date());
		return repository.save(request);
	}

	@Transactional
	public Request acceptUser(Integer idGroup, Integer idProfile) {
		Request auxRequest = repository
				.findById(new RequestPK(idGroup, idProfile))
				.orElseThrow(() -> new NoSuchElementException());

		GroupMemberDTO gm = new GroupMemberDTO();
		gm.setIdGroup(idGroup);
		gm.setIdProfile(idProfile);
		memberService.save(gm);

		auxRequest.setStatus(Status.ADMITTED);
		return repository.save(auxRequest);

	}

	@Transactional
	public void delete(RequestDTO requestDTO) {
		repository.deleteById(new RequestPK(requestDTO.getIdGroup(), requestDTO.getIdProfile()));
	}

	public Request convertToEntity(RequestDTO requestDTO) {
		return modelMapper.map(requestDTO, Request.class);
	}

	public RequestDTO convertToDTO(Request request) {
		return modelMapper.map(request, RequestDTO.class);
	}
}
