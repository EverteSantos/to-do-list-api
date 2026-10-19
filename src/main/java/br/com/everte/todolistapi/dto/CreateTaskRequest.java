package br.com.everte.todolistapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateTaskRequest {

    @Schema(
            description = "Título da tarefa",
            example = "Estudar Java"
    )
    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 100, message = "O título deve possuir no máximo 100 caracteres.")
    private String title;

    @Schema(
            description = "Descrição da tarefa",
            example = "Revisar conceitos de POO"
    )
    @Size(max = 500, message = "A descrição deve possuir no máximo 500 caracteres.")
    private String description;

    @FutureOrPresent(message = "O prazo não pode estar no passado.")
    @Schema(
            description = "Prazo para a conclusão da tarefa",
            example = "2026-10-20"
    )
    private LocalDate dueDate;



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}