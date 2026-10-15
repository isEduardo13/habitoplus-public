package com.habitoplus.habitoplusback.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.CommentDTO;
import com.habitoplus.habitoplusback.exception.UserNotMemberGroupException;
import com.habitoplus.habitoplusback.model.Comment;
import com.habitoplus.habitoplusback.model.CommentPK;
import com.habitoplus.habitoplusback.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;

    @Autowired
    private GroupMemberService memberService;

    @Autowired
    private ModelMapper modelMapper;

    public List<Comment> getAll(Integer idGroup) {
        return repository.findByGroupIdGroupOrderByDateTimeDesc(idGroup);
    }

    public List<Comment> getAll(Integer idGroup, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Comment> comments = repository.findByGroupIdGroupOrderByDateTimeDesc(idGroup, pageReq);
        return comments.getContent();
    }


    @Transactional
    public Comment save(CommentDTO comment) {
        if (memberService.getById(comment.getIdGroup(), comment.getIdProfile()) == null)
            throw new UserNotMemberGroupException("This user is not a member of the group");
        Comment commentTemp = convertToEntity(comment);
        commentTemp.setDateTime(LocalDateTime.now());

        return repository.save(commentTemp);
    }

    @Transactional
    public Comment update(Integer idGroup, Integer idProfile, LocalDateTime dateTime, CommentDTO commentDTO) {
        if (memberService.getById(idGroup, idProfile) == null)
            throw new UserNotMemberGroupException("This user is not a member of the group");
                    
        Comment auxComment = repository.findById(new CommentPK(idGroup, idProfile, dateTime)).orElseThrow(
            () -> new NoSuchElementException());

        auxComment.setContent(commentDTO.getContent());
        return repository.save(auxComment);
    }

    @Transactional
    public void delete(Integer idGroup, Integer idProfile, LocalDateTime dateTime) {
        if (memberService.getById(idGroup, idProfile) == null)
            throw new UserNotMemberGroupException("This user is not a member of the group");

        repository.deleteById(new CommentPK(idGroup, idProfile, dateTime));
    }

    public Comment convertToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    public CommentDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

}
