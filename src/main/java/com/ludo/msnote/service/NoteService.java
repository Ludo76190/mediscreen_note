package com.ludo.msnote.service;

import com.ludo.msnote.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    List<Note> getAll();
    List<Note> getNotesByPatientId(int patientId);
    Note findNote(String noteId);
    Note saveNote(Note noteToSave);
    Note updateNote(Note note);
    void deleteNote(String id);
}
