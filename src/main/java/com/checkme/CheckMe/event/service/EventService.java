package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.repository.EventRepository;
import com.checkme.CheckMe.exception.BadRequestException;
import com.checkme.CheckMe.user.entity.User;
import com.checkme.CheckMe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    private EventService(EventRepository eventRepository, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    public List<Event> getEvents(String status, String category) {
        if (status.equals("all")) {
            if (category.equals("competition")) {
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getCategory() == Event.Category.COMPETITION)
                        .collect(Collectors.toList());
            } else if (category.equals("volunteer")) {
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getCategory() == Event.Category.VOLUNTEER)
                        .collect(Collectors.toList());
            } else if (category.equals("all")) {
                return eventRepository.findAll();
            }

            throw new BadRequestException("No events found with the given category.");
        } else if (status.equals("active")) {
            if (category.equals("competition")) {
                LocalDate today = LocalDate.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(today) >= 0)
                        .filter(event -> event.getCategory() == Event.Category.COMPETITION)
                        .collect(Collectors.toList());
            } else if (category.equals("volunteer")) {
                LocalDate today = LocalDate.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(today) >= 0)
                        .filter(event -> event.getCategory() == Event.Category.VOLUNTEER)
                        .collect(Collectors.toList());
            }
            throw new BadRequestException("No events found with the given category.");

        } else if (status.equals("unactive")) {
            if (category.equals("competition")) {
                LocalDate today = LocalDate.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(today) <= 0)
                        .filter(event -> event.getCategory() == Event.Category.COMPETITION)
                        .collect(Collectors.toList());
            } else if (category.equals("volunteer")) {
                LocalDate today = LocalDate.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(today) <= 0)
                        .filter(event -> event.getCategory() == Event.Category.VOLUNTEER)
                        .collect(Collectors.toList());
            }
            throw new BadRequestException("No events found with the given  category.");
        }
        throw new BadRequestException("No events found with the given status.");
    }

    public List<Event> deleteEvent(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new BadRequestException("Event not found");
        }
        eventRepository.deleteById(id);
        return null;
    }

    public void addNewEvent(Event event) throws IllegalAccessException {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();

        if (user.getOrganizer() == null) {
            throw new BadRequestException("User have not an organizer yet");
        }
        // Create organizer
         var events = Event.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                 .category(event.getCategory())
                 .location(event.getLocation())
                 .poster(event.getPoster())
                 .registerUrl(event.getRegisterUrl())
                 .user(user)
                 .views(event.getViews())
                 .deadline(event.getDeadline())
                .build();
        // Save organizer
        eventRepository.save(events);
        // Update user


    }
    public void updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setCategory(updatedEvent.getCategory());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setPoster(updatedEvent.getPoster());
        existingEvent.setRegisterUrl(updatedEvent.getRegisterUrl());
        existingEvent.setViews(updatedEvent.getViews());
        existingEvent.setDeadline(updatedEvent.getDeadline());
        eventRepository.save(existingEvent);
    }
    public void incrementViewCount(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
    }
}
