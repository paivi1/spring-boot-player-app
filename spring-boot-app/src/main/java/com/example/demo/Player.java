package com.example.demo;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private long elapsedTime; // in milliseconds
    
    private boolean isRunning;
    
    private Long startTime; // timestamp when timer started
    
    public Player() {
        this.elapsedTime = 0;
        this.isRunning = false;
    }
    
    public Player(String name) {
        this();
        this.name = name;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getElapsedTime() {
        return elapsedTime;
    }
    
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public void setRunning(boolean running) {
        isRunning = running;
    }
    
    public Long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    
    public long getCurrentTime() {
        if (isRunning && startTime != null) {
            return elapsedTime + (Instant.now().toEpochMilli() - startTime);
        }
        return elapsedTime;
    }
}
