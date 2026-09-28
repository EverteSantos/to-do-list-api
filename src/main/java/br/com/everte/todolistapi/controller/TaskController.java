package br.com.everte.todolistapi.controller;

import br.com.everte.todolistapi.dto.CreateTaskRequest;
import br.com.everte.todolistapi.dto.UpdateStatusRequest;
import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.service.TaskService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
        name = "Tarefas",
        description = "Endpoints para gerenciamento de tarefas"
)
public class TaskController {

        private  final TaskService taskService;

        public TaskController(TaskService taskService){
            this.taskService = taskService;
        }

        @Operation(
                summary = "Criar uma nova tarefa",
                description = "Cria uma nova tarefa com título e descrição."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "201",
                        description = "Tarefa criada com sucesso"
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Dados da tarefa inválidos"
                )
        })
        @PostMapping
        public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
            Task task = taskService.createTask(
                    request.getTitle(),
                    request.getDescription()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        }

        @Operation(
                summary = "Listar tarefas",
                description = "Lista todas as tarefas existentes."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista de tarefas retornada com sucesso"
                )
        })
        @GetMapping
        public ResponseEntity<List<Task>> listTask(){
            return ResponseEntity.ok(taskService.listTasks());
        }

        @Operation(
                summary = "Buscar tarefa por ID",
                description = "Busca uma tarefa pelo ID informado."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200",
                        description = "Tarefa encontrada com sucesso"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tarefa não encontrada"
                )
        })
        @GetMapping("/{id}")
        public ResponseEntity<Task> searchTaskById(
                @Parameter(
                        description = "ID da tarefa",
                        example = "1"
                )
                @PathVariable Long id){
            return ResponseEntity.ok(taskService.searchTaskById(id));
        }

        @Operation(
                summary = "Atualizar status da tarefa",
                description = "Atualiza o status da tarefa pelo ID informado."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200",
                        description = "Tarefa atualizada com sucesso"
                ),
                @ApiResponse(
                         responseCode = "400",
                         description = "Status informado é inválido"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tarefa não encontrada"
                )
        })
        @PatchMapping("/{id}/status")
        public  ResponseEntity<Task> updateStatus(
                @Parameter(
                        description = "ID da tarefa",
                        example = "1"
                )
                @PathVariable long id,
                @Valid @RequestBody UpdateStatusRequest request) {

            Task task = taskService.updateStatus(
                    id,
                    request.getStatus()
            );

            return ResponseEntity.ok(task);
        }

        @Operation(
                summary = "Excluir tarefa",
                description = "Exclui tarefa buscando pelo ID informado."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "204",
                        description = "Tarefa excluída com sucesso"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Tarefa não encontrada"
                )
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTask(
                @Parameter(
                        description = "ID da tarefa",
                        example = "1"
                )
                @PathVariable Long id){
            taskService.deleteTask(id);

            return ResponseEntity.noContent().build();
        }



}
