package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.event.entity.FavoriteEvent;
import com.checkme.CheckMe.event.repository.FavoriteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class FavoriteEventServiceImpl implements FavoriteEventService {
    private final FavoriteEventRepository favoriteEventRepository;
    @Autowired
    public FavoriteEventServiceImpl(FavoriteEventRepository favoriteEventRepository) {
        this.favoriteEventRepository = favoriteEventRepository;
    }
    @Override
    public void addFavoriteEvent(FavoriteEvent favoriteEvent) {
        favoriteEvent.setCreatedAt(LocalDateTime.now());
        favoriteEventRepository.save(favoriteEvent);
    }
    @Override
    public void removeFavoriteEvent(Long id) {
        favoriteEventRepository.deleteById(id);
    }
}
