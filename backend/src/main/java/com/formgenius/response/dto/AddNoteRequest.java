package com.formgenius.response.dto;

import jakarta.validation.constraints.NotBlank;

public class AddNoteRequest {
    @NotBlank(message = "Note text is required")
    private String noteText;

    public AddNoteRequest() {}

    public AddNoteRequest(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
