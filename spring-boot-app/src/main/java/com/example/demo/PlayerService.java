package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;
    
    public Player createPlayer(String name) {
        Player player = new Player(name);
        return playerRepository.save(player);
    }
    
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
    
    public Optional<Player> getPlayer(Long id) {
        return playerRepository.findById(id);
    }
    
    public Player startTimer(Long id) {
        Player player = playerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Player not found"));
        
        if (!player.isRunning()) {
            player.setStartTime(Instant.now().toEpochMilli());
            player.setRunning(true);
            return playerRepository.save(player);
        }
        return player;
    }
    
    public Player stopTimer(Long id) {
        Player player = playerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Player not found"));
        
        if (player.isRunning()) {
            long currentTime = Instant.now().toEpochMilli();
            long sessionTime = currentTime - player.getStartTime();
            player.setElapsedTime(player.getElapsedTime() + sessionTime);
            player.setRunning(false);
            player.setStartTime(null);
            return playerRepository.save(player);
        }
        return player;
    }
    
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }
}
