package com.ludo.msnote.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private String id;

    @NotNull
    private Integer patientId;

    @NotNull
    private String note;
}
