package br.com.everte.todolistapi.controller;

import br.com.everte.todolistapi.dto.CreateTaskRequest;
import br.com.everte.todolistapi.dto.UpdateStatusRequest;
import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.service.TaskService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        private  final TaskService taskService;

        public TaskController(TaskService taskService){
            this.taskService = taskService;
        }

        @PostMapping
        public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
            Task task = taskService.creatTask(
                    request.getTitle(),
                    request.getDescription()
            );

            return ResponseEntity.status(201).body(task);
        }

        @GetMapping
        public ResponseEntity<List<Task>> listTask(){
            return ResponseEntity.ok(taskService.listTasks());
        }

        @PatchMapping("/{id}/status")
        public  ResponseEntity<Task> updateStatus(
                @PathVariable long id, @Valid @RequestBody UpdateStatusRequest request) {
            Task task = taskService.updateStatus(
                    id,
                    request.getStatus()
            );

            return ResponseEntity.ok(task);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTask(@PathVariable Long id){
            taskService.deleteTask(id);

            return ResponseEntity.noContent().build();
        }



}
