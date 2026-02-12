package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    
    @Autowired
    private PlayerService playerService;
    
    @PostMapping
    public Player createPlayer(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "Player");
        return playerService.createPlayer(name);
    }
    
    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }
    
    @PostMapping("/{id}/start")
    public Player startTimer(@PathVariable Long id) {
        return playerService.startTimer(id);
    }
    
    @PostMapping("/{id}/stop")
    public Player stopTimer(@PathVariable Long id) {
        return playerService.stopTimer(id);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
