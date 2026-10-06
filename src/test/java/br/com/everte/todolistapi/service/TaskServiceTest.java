package br.com.everte.todolistapi.service;

import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.enums.TaskPriority;
import br.com.everte.todolistapi.enums.TaskStatus;
import br.com.everte.todolistapi.exception.TaskNotFoundException;
import br.com.everte.todolistapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp(){
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void shouldCreateTask(){

        Task savedTask = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        Mockito.when(
                taskRepository.save(Mockito.any(Task.class))
        ).thenReturn(savedTask);

        Task result = taskService.createTask(
                "Estudar Java",
                "Revisar POO"
        );

        assertEquals("Estudar Java", result.getTitle());
        assertEquals("Revisar POO", result.getDescription());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
    }

    @Test
    void shouldListTasks(){
        Task task1 = new Task("Estudar Java", "POO");
        Task task2 = new Task("Estudar Spring", "API REST");

        Mockito.when(taskRepository.findAll())
                .thenReturn(List.of(task1,task2));

        List<Task> result = taskService.listTasks();

        assertEquals(2, result.size());
        assertEquals("Estudar Java", result.get(0).getTitle());
        assertEquals("Estudar Spring", result.get(1).getTitle());

    }

    @Test
    void shouldFindTaskById(){

        Task task = new Task(
                "Estudar Java",
                "Revisar Testes"
        );

        Mockito.when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.searchTaskById(1L);

        assertEquals("Estudar Java", result.getTitle());
        assertEquals("Revisar Testes", result.getDescription());

    }

    @Test
    void  shouldThrowExceptionWhenTaskDoesNotExist(){
        Mockito.when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.searchTaskById(999L)
        );
    }

    @Test
    void shouldUpdateTaskStatus(){
        Task task = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        Mockito.when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(task);

        Task result = taskService.updateStatus(
                1L,
                TaskStatus.COMPLETED
                );

        assertEquals(TaskStatus.COMPLETED, result.getStatus());

    }

    @Test
    void shouldUpdateTaskPriority(){
        Task task = new Task(
                "Estudar Java",
                "Revisar POO"
        );

        Mockito.when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(task);

        Task result = taskService.updatePriority(
                1L,
                TaskPriority.HIGH
        );

        assertEquals(TaskPriority.HIGH, result.getPriority());

    }

    @Test
    void shouldDeletTask(){
        Mockito.when(taskRepository.existsById(1L))
                .thenReturn(true);
        taskService.deleteTask(1L);

        Mockito.verify(taskRepository)
                .deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTask(){
        Mockito.when(taskRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(999L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTask() {

        Mockito.when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateStatus(
                        999L,
                        TaskStatus.COMPLETED
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriorityNonExistingTask() {

        Mockito.when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updatePriority(
                        999L,
                        TaskPriority.HIGH
                )
        );
    }

}
