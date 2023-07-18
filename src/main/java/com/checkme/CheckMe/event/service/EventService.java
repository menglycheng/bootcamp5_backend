package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.aws.AmazonClientService;
import com.checkme.CheckMe.event.dto.EventRequest;
import com.checkme.CheckMe.event.dto.UpdateEventRequest;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AmazonClientService amazonClientService;

    @Autowired
    private EventService(EventRepository eventRepository, UserRepository userRepository, AmazonClientService amazonClientService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.amazonClientService = amazonClientService;
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
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
            } else {
                throw new BadRequestException("No events found with the given category.");
            }
        } else if (status.equals("active")) {
            if (category.equals("competition")) {
                LocalDateTime now = LocalDateTime.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(now) >= 0)
                        .filter(event -> event.getCategory() == Event.Category.COMPETITION)
                        .collect(Collectors.toList());
            } else if (category.equals("volunteer")) {
                LocalDateTime now = LocalDateTime.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(now) >= 0)
                        .filter(event -> event.getCategory() == Event.Category.VOLUNTEER)
                        .collect(Collectors.toList());
            } else if (category.equals("all")) {
                LocalDateTime now = LocalDateTime.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(now) >= 0)
                        .collect(Collectors.toList());
            }
            throw new BadRequestException("No events found with the given category.");
        } else if (status.equals("unactive")) {
            if (category.equals("competition")) {
                LocalDateTime now = LocalDateTime.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(now) <= 0)
                        .filter(event -> event.getCategory() == Event.Category.COMPETITION)
                        .collect(Collectors.toList());
            } else if (category.equals("volunteer")) {
                LocalDateTime now = LocalDateTime.now();
                return eventRepository.findAll()
                        .stream()
                        .filter(event -> event.getDeadline().compareTo(now) <= 0)
                        .filter(event -> event.getCategory() == Event.Category.VOLUNTEER)
                        .collect(Collectors.toList());
            }
            throw new BadRequestException("No events found with the given category.");
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

    public void addNewEvent(EventRequest event) throws IllegalAccessException {
        // Get user from security context
        var userPrinciple = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Get user from database
        var user = userRepository.findById(userPrinciple.getId()).orElseThrow();

        if (user.getOrganizer() == null) {
            throw new BadRequestException("User does not have an organizer yet");
        }

        // Check if user uploaded a poster
        // Create folder path for event poster
        String folderPath = "event/" + user.getOrganizer().getName() + "/poster";
        // Upload the poster to AWS S3
        String posterUrl = amazonClientService.upload(event.getPoster(), folderPath);

        // Create event
        var newEvent = Event.builder()
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .location(event.getLocation())
                .poster(posterUrl)
                .registerUrl(event.getRegisterUrl())
                .user(user)
                .views(0)
                .deadline(LocalDateTime.of(event.getDeadline(), LocalTime.MIDNIGHT))
                .build();

        // Save event
        eventRepository.save(newEvent);
    }

    public void updateEvent(Long id, UpdateEventRequest updatedEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        existingEvent.setTitle(updatedEvent.getTitle() != null ? updatedEvent.getTitle() : existingEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription() != null ? updatedEvent.getDescription() :
                existingEvent.getDescription());
        existingEvent.setCategory(updatedEvent.getCategory() != null ? updatedEvent.getCategory() :
                existingEvent.getCategory());
        existingEvent.setLocation(updatedEvent.getLocation() != null ? updatedEvent.getLocation() :
                existingEvent.getLocation());
        existingEvent.setRegisterUrl(updatedEvent.getRegisterUrl() != null ? updatedEvent.getRegisterUrl() :
                existingEvent.getRegisterUrl());
        existingEvent.setDeadline(updatedEvent.getDeadline() != null ?
                LocalDateTime.of(updatedEvent.getDeadline(), LocalTime.MIDNIGHT) :
                existingEvent.getDeadline());
        // Check if user poster is updated
        if (updatedEvent.getPoster() != null) {
            // Create folder path for event poster
            String folderPath = "event/" + existingEvent.getUser().getOrganizer().getName() + "/poster";
            // Upload the poster to AWS S3
            String posterUrl = amazonClientService.upload(updatedEvent.getPoster(), folderPath);
            existingEvent.setPoster(posterUrl);
        }
        eventRepository.save(existingEvent);
    }

    public void incrementViewCount(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
    }

    public List<Event> getEventsByUsername(String username) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getUser().getUniqueUsername().equals(username))
                .collect(Collectors.toList());
    }
}
