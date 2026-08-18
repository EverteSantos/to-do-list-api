package br.com.everte.todolistapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {

    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 100, message = "O título deve possuir no máximo 100 caracteres.")
    private String title;

    @Size(max = 500, message = "A descrição deve possuir no máximo 500 caracteres.")
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
