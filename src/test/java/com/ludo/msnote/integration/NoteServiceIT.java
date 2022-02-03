package com.ludo.msnote.integration;

import com.ludo.msnote.model.Note;
import com.ludo.msnote.repository.NoteRepository;
import com.ludo.msnote.service.NoteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class NoteServiceIT {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    @BeforeEach
    public void cleanUp(){
        mongoTemplate.dropCollection(Note.class);
    }

    @Test
    public void saveDeleteNoteIT(){
        Note newNote = new Note();
        newNote.setPatientId(1);
        newNote.setNote("integration test note");

        noteService.saveNote(newNote);
        Assertions.assertEquals(1, noteRepository.findAll().size());

        noteService.deleteNote("noteId");
        Assertions.assertNull(noteService.findNote("noteId"));
    }



}
