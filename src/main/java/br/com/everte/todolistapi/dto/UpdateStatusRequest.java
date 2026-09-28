package br.com.everte.todolistapi.dto;

import br.com.everte.todolistapi.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @Schema(
            description = "Status da tarefa",
            example = "COMPLETED"
    )
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
