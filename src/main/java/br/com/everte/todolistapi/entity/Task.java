package br.com.everte.todolistapi.entity;

import br.com.everte.todolistapi.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;


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

    protected Task(){
    }

    public Task(String title, String description){
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
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

    public void updateStatus(TaskStatus status){
        this.status = status;
    }
}

