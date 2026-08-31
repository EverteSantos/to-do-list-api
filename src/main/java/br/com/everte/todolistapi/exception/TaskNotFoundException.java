package br.com.everte.todolistapi.exception;

public class TaskNotFoundException  extends RuntimeException{

    public  TaskNotFoundException(Long id){
        super("Tarefa com ID " + id + " não encontrada.");
    }
}
