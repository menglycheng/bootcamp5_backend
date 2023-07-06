package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.event.entity.FavoriteEvent;

public interface FavoriteEventService {
    void addFavoriteEvent(FavoriteEvent favoriteEvent);
    void removeFavoriteEvent(Long id);
}
