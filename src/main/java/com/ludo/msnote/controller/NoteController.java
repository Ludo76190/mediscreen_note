package com.ludo.msnote.controller;

import com.ludo.msnote.Dto.NoteDto;
import com.ludo.msnote.model.Note;
import com.ludo.msnote.service.NoteService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/note")
@RestController
public class NoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NoteService noteService;

    @GetMapping("/")
    public String index() {
        return "Greeting to Note Microservice";
    }

    @ApiOperation(value = "get all notes as a list")
    @GetMapping("/list")
    public List<NoteDto> getAll(){
        LOGGER.info("GET /note/list ");
        return noteService.getAll().stream().map(note -> modelMapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "get all notes for the given patient based on its id as path variable")
    @GetMapping("/patient/{id}")
    public List<NoteDto> getNotesForPatientId(@PathVariable("id") int id){
        LOGGER.info("GET /note/patient/{id} patientId=" + id);
        return noteService.getNotesByPatientId(id).stream().map(note -> modelMapper.map(note, NoteDto.class))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get a note by its id")
    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> findNote(@PathVariable("id") String id){
        LOGGER.info("GET /note/id noteId=" + id);

        Note note = noteService.findNote(id);
        if (note != null) {
            NoteDto noteResponse = modelMapper.map(note, NoteDto.class);
            return ResponseEntity.ok().body(noteResponse);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "create a new note")
    @PostMapping("/add")
    public ResponseEntity<NoteDto> saveNote(@Valid @RequestBody NoteDto noteDto){
        LOGGER.info("POST /note/add newNote= " + noteDto.getId());
        Note noteRequest = modelMapper.map(noteDto, Note.class);
        Note note = noteService.saveNote(noteRequest);
        NoteDto savedNote = modelMapper.map(note, NoteDto.class);
        return new ResponseEntity<NoteDto>(savedNote, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update an existing note")
    @PostMapping("/update")
    public ResponseEntity<NoteDto> updateNote(@Valid @RequestBody NoteDto noteToUpdate) {
        LOGGER.info("POST /note/update noteToUpdate id = " + noteToUpdate.getId());
        Note noteRequest = modelMapper.map(noteToUpdate, Note.class);
        Note note = noteService.updateNote(noteRequest);
        NoteDto noteResponse = modelMapper.map(note, NoteDto.class);
        return new ResponseEntity<NoteDto>(noteResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a note")
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        LOGGER.info("POST /note/delete noteToDelete id = " + id);
        noteService.deleteNote(id);
    }

}
