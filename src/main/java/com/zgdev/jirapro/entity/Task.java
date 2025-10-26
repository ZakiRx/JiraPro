package com.zgdev.jirapro.entity;

import jakarta.persistence.*;

@Entity()
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String label;
    private String description;
    @ManyToOne
    private TaskStatus completed;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getCompleted() {
        return completed;
    }

    public void setCompleted(TaskStatus completed) {
        this.completed = completed;
    }

}
