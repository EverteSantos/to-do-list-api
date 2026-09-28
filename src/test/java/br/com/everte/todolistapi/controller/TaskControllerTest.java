package br.com.everte.todolistapi.controller;

import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.enums.TaskStatus;
import br.com.everte.todolistapi.exception.TaskNotFoundException;
import br.com.everte.todolistapi.service.TaskService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldCreateTask() throws Exception {

        Task task = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        when(taskService.createTask(
                "Estudar Java",
                "Revisar POO"
        )).thenReturn(task);

        mockMvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "title": "Estudar Java",
                                    "description": "Revisar POO"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$.description")
                        .value("Revisar POO"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }

    @Test
    void shouldListTasks() throws Exception {

        Task task1 = new Task(
                "Estudar Java",
                "POO"
        );

        Task task2 = new Task(
                "Estudar Spring",
                "API REST"
        );

        when(taskService.listTasks())
                .thenReturn(List.of(task1, task2));

        mockMvc.perform(
                        get("/api/tasks")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(2))
                .andExpect(jsonPath("$[0].title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$[1].title")
                        .value("Estudar Spring"));
    }

    @Test
    void shouldFindTaskById() throws Exception {

        Task task = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        when(taskService.searchTaskById(1L))
                .thenReturn(task);

        mockMvc.perform(
                        get("/api/tasks/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$.description")
                        .value("Revisar POO"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }

    @Test
    void shouldReturn404WhenTaskDoesNotExist() throws Exception {

        when(taskService.searchTaskById(999L))
                .thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(
                        get("/api/tasks/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Tarefa com ID 999 não encontrada."));
    }

    @Test
    void shouldUpdateStatus() throws Exception {
        Task task = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        task.updateStatus(TaskStatus.COMPLETED);

        when(taskService.updateStatus(eq(1L),
                eq(TaskStatus.COMPLETED)))
                .thenReturn(task);

        mockMvc.perform(
                patch("/api/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "status": "COMPLETED"
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$.description")
                        .value("Revisar POO"))
                .andExpect(jsonPath("$.status")
                        .value("COMPLETED"));

        verify(taskService).updateStatus(
                1L,
                TaskStatus.COMPLETED
        );


    }

    @Test
    void shouldDeleteTask() throws Exception {

        mockMvc.perform(
                        delete("/api/tasks/1")
                )
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);

    }

    @Test
    void shouldReturn400WhenCreateTaskIsInvalid() throws Exception {

        mockMvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "title": "",
                                "description": "Revisar POO"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("O título é obrigatório."));
    }

    @Test
    void shouldReturn400WhenStatusIsInvalid() throws Exception {

        mockMvc.perform(
                        patch("/api/tasks/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "status": null
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value("O status é obrigatório."));
    }

    @Test
    void shouldReturn404WhenUpdatingTaskDoesNotExist() throws Exception {

        when(taskService.updateStatus(
                eq(999L),
                eq(TaskStatus.COMPLETED)
        )).thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(
                        patch("/api/tasks/999/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "status": "COMPLETED"
                            }
                            """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Tarefa com ID 999 não encontrada."));
    }

    @Test
    void shouldReturn404WhenDeletingTaskDoesNotExist() throws Exception {

        doThrow(new TaskNotFoundException(999L))
                .when(taskService)
                .deleteTask(999L);

        mockMvc.perform(
                        delete("/api/tasks/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Tarefa com ID 999 não encontrada."));
    }

}