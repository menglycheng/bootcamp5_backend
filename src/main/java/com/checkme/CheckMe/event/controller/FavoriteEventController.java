package com.checkme.CheckMe.event.controller;

import com.checkme.CheckMe.event.entity.FavoriteEvent;
import com.checkme.CheckMe.event.service.FavoriteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite-events")
public class FavoriteEventController {
    private final FavoriteEventService favoriteEventService;

    @Autowired
    public FavoriteEventController(FavoriteEventService favoriteEventService) {
        this.favoriteEventService = favoriteEventService;
    }

    @PostMapping("/add")
    public void addFavoriteEvent(@RequestParam Long eventId) {
        favoriteEventService.saveFavoriteEvent(eventId);
    }
    @GetMapping
    public List<FavoriteEvent> getFavoriteEventsByUser(@RequestParam("username") String username) {
        return favoriteEventService.getFavoriteEventsByUser(username);
    }
    @DeleteMapping("/delete")
    public void deleteFavEvent(@RequestParam("id") Long id) {
        favoriteEventService.deleteFavEvent(id);
    }

}
