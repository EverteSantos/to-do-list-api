package br.com.everte.todolistapi.controller;

import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.enums.TaskPriority;
import br.com.everte.todolistapi.enums.TaskStatus;
import br.com.everte.todolistapi.exception.TaskNotFoundException;
import br.com.everte.todolistapi.service.TaskService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
                "Revisar POO",
                LocalDate.of(2026,10,20)
        );

        when(taskService.createTask(
                "Estudar Java",
                "Revisar POO",
                LocalDate.of(2026,10,20)
        )).thenReturn(task);

        mockMvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "title": "Estudar Java",
                                    "description": "Revisar POO",
                                    "dueDate": "2026-10-20"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$.description")
                        .value("Revisar POO"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"))
                .andExpect(jsonPath("$.priority")
                        .value("MEDIUM"))
                .andExpect(jsonPath("$.dueDate")
                        .value("2026-10-20"));
    }

    @Test
    void shouldListTasks() throws Exception {

        Task task1 = new Task(
                "Estudar Java",
                "POO",
                LocalDate.of(2026,10,20)
        );

        Task task2 = new Task(
                "Estudar Spring",
                "API REST",
                LocalDate.of(2026,10,20)
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
                "Revisar POO",
                LocalDate.of(2026,10,20)
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
                        .value("PENDING"))
                .andExpect(jsonPath("$.dueDate")
                        .value("2026-10-20"));;
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
    void shouldReturn400WhenDueDateIsInThePast() throws Exception {


        mockMvc.perform(
                        post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "title": "Estudar Java",
                                        "description": "Revisar POO",
                                        "dueDate": "2020-01-01"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dueDate")
                        .value("O prazo não pode estar no passado.")
                );

    }

    @Test
    void shouldUpdateStatus() throws Exception {
        Task task = new Task(
                "Estudar Java",
                "Revisar POO",
                LocalDate.of(2026,10,20)
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
                        .value("COMPLETED"))
                .andExpect(jsonPath("$.dueDate")
                        .value("2026-10-20"));

        verify(taskService).updateStatus(
                1L,
                TaskStatus.COMPLETED
        );


    }

    @Test
    void shouldUpdatePriority() throws Exception {
        Task task = new Task(
                "Estudar Java",
                "Revisar POO",
                LocalDate.of(2026,10,20)
        );

        task.updatePriority(TaskPriority.HIGH);

        when(taskService.updatePriority(eq(1L),
                eq(TaskPriority.HIGH)))
                .thenReturn(task);

        mockMvc.perform(
                        patch("/api/tasks/1/priority")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "priority": "HIGH"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Estudar Java"))
                .andExpect(jsonPath("$.description")
                        .value("Revisar POO"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.dueDate")
                        .value("2026-10-20"));;

        verify(taskService).updatePriority(
                1L,
                TaskPriority.HIGH
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
    void shouldReturn400WhenPriorityIsInvalid() throws Exception {

        mockMvc.perform(
                        patch("/api/tasks/1/priority")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "priority": null
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.priority")
                        .value("A prioridade é obrigatória."));
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
    void shouldReturn404WhenUpdatingPriorityTaskDoesNotExist() throws Exception {

        when(taskService.updatePriority(
                eq(999L),
                eq(TaskPriority.HIGH)
        )).thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(
                        patch("/api/tasks/999/priority")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "priority": "HIGH"
                        }
                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Tarefa com ID 999 não encontrada."));
    }

    @Test
    void shouldUpdateDueDate() throws Exception{
        Task task = new Task("Estudar Java",
                "Revisar POO",
                LocalDate.of(2026, 10, 20));
        task.updateDueDate(LocalDate.of(2026, 10, 30));

        when(taskService.updateDueDate(
                eq(1L),
                eq(LocalDate.of(2026, 10, 30))
        )).thenReturn(task);

        mockMvc.perform(
                patch("/api/tasks/1/due-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "dueDate": "2026-10-30"
                                }
                               """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dueDate")
                        .value("2026-10-30"));

        verify(taskService).updateDueDate(
                1L,
                LocalDate.of(2026, 10, 30)
        );
    }

    @Test
    void shouldReturn400WhenUpdateDueDateIsInThePast() throws Exception{

        mockMvc.perform(
                patch("/api/tasks/1/due-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "dueDate": "2021-01-01"
                                }
                                """)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dueDate")
                        .value("O prazo não pode estar no passado."));
    }

    @Test
    void shouldReturn404WhenUpdatingDueDateTaskDoesNotExist() throws Exception{
        when(taskService.updateDueDate(
                eq(999L),
                eq(LocalDate.of(2026, 10, 30))
        )).thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(
                patch("/api/tasks/999/due-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "dueDate": "2026-10-30"
                                }
                                """)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Tarefa com ID 999 não encontrada."));
    }

}