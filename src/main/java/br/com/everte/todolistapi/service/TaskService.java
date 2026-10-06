package br.com.everte.todolistapi.service;

import br.com.everte.todolistapi.entity.Task;
import br.com.everte.todolistapi.enums.TaskPriority;
import br.com.everte.todolistapi.enums.TaskStatus;
import br.com.everte.todolistapi.repository.TaskRepository;
import br.com.everte.todolistapi.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task createTask(String title, String description){
        Task task = new Task(title, description);

        return taskRepository.save(task);
    }

    public List<Task> listTasks(){
        return taskRepository.findAll();
    }

    public Task searchTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateStatus(Long id, TaskStatus status){
        Task task = searchTaskById(id);

        task.updateStatus(status);

        return taskRepository.save(task);
    }


    public Task updatePriority(Long id, TaskPriority priority){
        Task task = searchTaskById(id);

        task.updatePriority(priority);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        if (!taskRepository.existsById(id)){
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
