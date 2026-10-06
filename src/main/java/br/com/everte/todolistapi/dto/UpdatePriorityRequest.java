package br.com.everte.todolistapi.dto;


import br.com.everte.todolistapi.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UpdatePriorityRequest {

    @Schema(
            description = "Prioridade da tarefa",
            example = "MEDIUM"
    )
    @NotNull(message = "A prioridade é obrigatória.")
    private TaskPriority priority;

    public UpdatePriorityRequest (){

    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority){
        this.priority = priority;
    }
}
