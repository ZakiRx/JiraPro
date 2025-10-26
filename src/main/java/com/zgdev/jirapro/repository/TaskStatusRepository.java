package com.zgdev.jirapro.repository;

import com.zgdev.jirapro.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus,Long> {

    Optional<TaskStatus> getTaskStatusByStatus(String status);
}
