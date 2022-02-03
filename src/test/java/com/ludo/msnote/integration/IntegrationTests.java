package com.ludo.msnote.integration;

import com.ludo.msnote.Dto.NoteDto;
import com.ludo.msnote.controller.NoteController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationTests {

    @Autowired
    NoteController noteController;

    @Test
    public void testCreateReadDelete() {
        NoteDto note = new NoteDto("noteId", 1 , "comment");

        ResponseEntity<NoteDto> noteResult = noteController.saveNote(note);

        noteController.delete("noteId");
        Assertions.assertThat(noteController.getNotesForPatientId(1)).isEmpty();

    }
}
