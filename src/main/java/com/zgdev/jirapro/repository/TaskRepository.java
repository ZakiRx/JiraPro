package com.zgdev.jirapro.repository;

import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t JOIN FETCH t.completed ts WHERE ts = :completed")
    Optional<List<Task>> getTasksByCompleted(@Param("completed") TaskStatus completed);
}
