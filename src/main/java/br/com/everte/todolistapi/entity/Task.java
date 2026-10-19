package br.com.everte.todolistapi.entity;

import br.com.everte.todolistapi.enums.TaskPriority;
import br.com.everte.todolistapi.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;

import java.time.LocalDate;


@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    private LocalDate dueDate;

    protected Task(){
    }

    public Task(String title, String description, LocalDate dueDate){
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.priority = TaskPriority.MEDIUM;
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority(){ return priority; }

    public LocalDate getDueDate() { return dueDate; }

    public void updateStatus(TaskStatus status){
        this.status = status;
    }

    public void updatePriority(TaskPriority priority){ this.priority = priority; }

    public void updateDueDate(LocalDate dueDate){ this.dueDate = dueDate; }
}

