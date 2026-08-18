package br.com.everte.todolistapi.dto;

import br.com.everte.todolistapi.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "O status é obrigatório.")
    private TaskStatus status;

    public UpdateStatusRequest(){

    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
