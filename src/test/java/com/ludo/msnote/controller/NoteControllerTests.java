package com.ludo.msnote.controller;

import com.ludo.msnote.model.Note;
import com.ludo.msnote.service.NoteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NoteControllerTests {

    @MockBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    private static final Note note = new Note();
    private static final String NOTE_AS_JSON = "{\"id\":\"noteId\",\"patientId\":1,\"note\":\"medical comments\"}";

    @BeforeAll
    static void setUp(){
        note.setId("noteId");
        note.setPatientId(1);
        note.setNote("medical comments");
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/note/")).andExpect(status().isOk());
    }

    @Test
    public void testFindNoteById() throws Exception {
        when(noteService.findNote("noteId")).thenReturn(note);
        mockMvc.perform(get("/note/noteId"))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllShouldReturn200AndAListOfNoteDTO() throws Exception {
        Mockito.when(noteService.getAll()).thenReturn(Collections.singletonList(note));

        mockMvc.perform(get("/note/list"))
                .andExpect(mvcResult -> {
                    Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains(NOTE_AS_JSON));
                });
    }

    @Test
    public void getNotesForPatientIdShouldReturn200AndAListOfNoteDTO() throws Exception {
        Mockito.when(noteService.getNotesByPatientId(1)).thenReturn(Collections.singletonList(note));

        mockMvc.perform(get("/note/patient/1"))
                .andExpect(mvcResult -> {
                    Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains(NOTE_AS_JSON));
                });
    }

    @Test
    public void saveNoteShouldReturn201AndNewNoteDTO() throws Exception {
        Mockito.when(noteService.saveNote(any(Note.class))).thenReturn(note);

        String newNoteBody = "{\"patientId\":1,\"note\":\"medical comments\"}";

        mockMvc.perform(post("/note/add").contentType(MediaType.APPLICATION_JSON).content(newNoteBody))
                .andExpect(mvcResult -> {
                    Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains(NOTE_AS_JSON));
                });
    }

    @Test
    public void saveNoteShouldReturn400WhenNoteDtoNotValid() throws Exception {
        mockMvc.perform(post("/note/add").contentType(MediaType.APPLICATION_JSON).content("{\"validJson\":\"but not a valid note\"}"))
                .andExpect(mvcResult -> Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus()));
    }

    @Test
    public void findNoteShouldReturn200AndANoteDto() throws Exception {
        Mockito.when(noteService.getNotesByPatientId(any(int.class))).thenReturn(Collections.singletonList(note));

        mockMvc.perform(get("/note/patient/1"))
                .andExpect(mvcResult -> {
                   Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
                   Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains(NOTE_AS_JSON));
                });
    }

    @Test
    public void findNoteShouldReturn404() throws Exception {
        Mockito.when(noteService.findNote("notFoundId")).thenReturn(null);

        mockMvc.perform(get("/note/" + "notFoundId"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateNoteShouldReturn200AndUpdatedNote() throws Exception {
        Mockito.when(noteService.updateNote(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/note/update/")
                                .content(NOTE_AS_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNoteShouldReturn400() throws Exception {
        mockMvc.perform(post("/note/update").contentType(MediaType.APPLICATION_JSON).content("{\"validJson\":\"but not a valid note\"}"))
                .andExpect(mvcResult -> Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus()));
    }
}
