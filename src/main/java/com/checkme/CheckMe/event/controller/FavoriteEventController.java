package com.checkme.CheckMe.event.controller;

import com.checkme.CheckMe.event.entity.FavoriteEvent;
import com.checkme.CheckMe.event.service.FavoriteEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite-events")
public class FavoriteEventController {
    private final FavoriteEventService favoriteEventService;
    @Autowired
    public FavoriteEventController(FavoriteEventService favoriteEventService) {
        this.favoriteEventService = favoriteEventService;
    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addFavoriteEvent(@RequestBody FavoriteEvent favoriteEvent) {
        favoriteEventService.addFavoriteEvent(favoriteEvent);
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavoriteEvent(@PathVariable Long id) {
        favoriteEventService.removeFavoriteEvent(id);
    }
}
