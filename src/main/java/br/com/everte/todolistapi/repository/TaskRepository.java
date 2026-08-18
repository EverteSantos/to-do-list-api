package br.com.everte.todolistapi.repository;

import br.com.everte.todolistapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
