package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.entity.FavoriteEvent;
import com.checkme.CheckMe.event.repository.EventRepository;
import com.checkme.CheckMe.event.repository.FavoriteEventRepository;
import com.checkme.CheckMe.exception.BadRequestException;
import com.checkme.CheckMe.user.entity.User;
import com.checkme.CheckMe.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteEventService {
    private final EventRepository eventRepository;
    private final FavoriteEventRepository favoriteEventRepository;
    private final UserRepository userRepository;

    public FavoriteEventService(EventRepository eventRepository, FavoriteEventRepository favoriteEventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.favoriteEventRepository = favoriteEventRepository;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new IllegalStateException("Invalid user principal type");
        }

        User currentUser = (User) principal;
        return currentUser;
    }

    public FavoriteEvent saveFavoriteEvent(Long eventId) {
        User currentUser = getCurrentUser();

        // Retrieve the user from the database
        Optional<User> optionalUser = userRepository.findById(currentUser.getId());
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("User not found"));

        // Retrieve the event from the database
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new NoSuchElementException("Event not found"));

        // Create a new favorite event
        FavoriteEvent favoriteEvent = FavoriteEvent.builder()
                .user(user)
                .event(event)
                .build();

        // Save the favorite event
        FavoriteEvent savedFavoriteEvent = favoriteEventRepository.save(favoriteEvent);

        return savedFavoriteEvent;
    }

    public List<FavoriteEvent> getFavoriteEventsByUser(String username) {
        return favoriteEventRepository.findAll().stream()
                .filter(favEvent -> favEvent.getUser().getUniqueUsername().equals(username))
                .collect(Collectors.toList());
    }
    public List<Event> deleteFavEvent(Long id) {
        if (favoriteEventRepository.findById(id).isEmpty()) {
            throw new BadRequestException("Event not found");
        }
        favoriteEventRepository.deleteById(id);
        return null;
    }

}
