package br.com.everte.todolistapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public class UpdateDueDateRequest {

    @FutureOrPresent(message = "O prazo não pode estar no passado.")
    @Schema(
            description = "Prazo da tarefa",
            example = "2026-10-20"
    )

    private LocalDate dueDate;

    public UpdateDueDateRequest(){

    }

    public LocalDate getDueDate () {
        return dueDate;
    }

    public void setDueDate (LocalDate dueDate){
        this.dueDate = dueDate;
    }
}
