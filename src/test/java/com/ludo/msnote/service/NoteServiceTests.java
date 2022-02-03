package com.ludo.msnote.service;

import com.ludo.msnote.model.Note;
import com.ludo.msnote.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class NoteServiceTests {
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private static final Note note = new Note();

    @BeforeAll
    static void setUp(){
        note.setId("noteId");
        note.setPatientId(1);
        note.setNote("medical comments");
    }

    @Test
    public void getAllTest(){
        Mockito.when(noteRepository.findAll()).thenReturn(Collections.singletonList(note));

        List<Note> result = noteService.getAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("noteId", result.get(0).getId());
    }

    @Test
    public void getNotesByPatientIdTest(){
        Mockito.when(noteRepository.findAllByPatientId(1)).thenReturn(Collections.singletonList(note));

        List<Note> result = noteService.getNotesByPatientId(1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("noteId", result.get(0).getId());
    }

    @Test
    public void saveNoteTest(){
        Note noteToSave = new Note();
        noteToSave.setPatientId(1);
        noteToSave.setNote("medical comments");

        Mockito.when(noteRepository.save(noteToSave)).thenReturn(note);

        Note result = noteService.saveNote(noteToSave);

        Assertions.assertEquals(note.getId(), result.getId());
    }

    @Test
    public void updateNoteTest() {
        when(noteRepository.findById("noteId")).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);
        System.out.println("note = " + note);

        noteService.updateNote(note);

        verify(noteRepository, times(1)).save(note);


    }

    @Test
    public void findNoteOKTest(){
        Mockito.when(noteRepository.findById("noteId")).thenReturn(Optional.of(note));

        Assertions.assertEquals(1, (int) noteService.findNote("noteId").getPatientId());
    }

    @Test
    public void findNoteNotFoundTest(){
        Mockito.when(noteRepository.findById("badId")).thenReturn(Optional.empty());

        Assertions.assertNull(noteService.findNote("badId"));
    }

    @Test
    void TestDeleteNote() {
        when(noteRepository.findById("noteId")).thenReturn(Optional.of(note));
        noteService.deleteNote("noteId");

        verify(noteRepository, times(1)).findById("noteId");
    }

}
