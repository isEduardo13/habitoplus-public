package com.habitoplus.habitoplusback.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.NoteDTO;
import com.habitoplus.habitoplusback.dto.UpdateNoteDTO;
import com.habitoplus.habitoplusback.exception.UserNotFoundException;
import java.util.NoSuchElementException;
import com.habitoplus.habitoplusback.model.Note;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.NoteRepository;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ProfileService profileService;

    public Note getNoteById(int idNote) {
        return noteRepository.findById(idNote)
                .orElseThrow(() -> new NoSuchElementException("Note not found with id: " + idNote));
    }

    public Page<Note> getAll(Integer profileId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return noteRepository.findByProfileId(profileId, pageRequest);
    }

    public void deleteNote(int idNote) {
        if (!noteRepository.existsById(idNote)) {
            throw new NoSuchElementException("Note not found with id: " + idNote);
        }
        noteRepository.deleteById(idNote);
    }

    public void addNote(NoteDTO createNoteDTO) {
        Note note = new Note();
        note.setTitle(createNoteDTO.getTitle());
        note.setContent(createNoteDTO.getContent());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(formatter);
        note.setDate(formattedDate);
        
        Profile profile = profileService.getProfileById(createNoteDTO.getIdProfile());
        if (profile == null) {
            throw new UserNotFoundException("Profile not found with id: " + createNoteDTO.getIdProfile());
        }
        note.setProfile(profile);
        noteRepository.save(note);
    }
    

    public void updateNoteById(UpdateNoteDTO updateNoteDTO) {
        Note note = noteRepository.findById(updateNoteDTO.getIdNote())
                .orElseThrow(() -> new NoSuchElementException("Note not found with id: " + updateNoteDTO.getIdNote()));

        note.setTitle(updateNoteDTO.getTitle());
        note.setContent(updateNoteDTO.getContent());
        noteRepository.save(note);
    }
}
