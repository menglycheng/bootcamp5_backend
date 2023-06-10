package com.checkme.CheckMe.event.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name= "Event")
@Table
public class Event {
    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )

    private Long id;
    @Column(
            name="title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;
    @Column(
            name="organization_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String organizationName;
    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(
            name="category",
            nullable = false
    )
    private Category category;
    @Column(
            name="location",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String location;
    @Column(
            name="poster",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String poster;
    @Column(
            name="register_url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String registerUrl;

    private int views;
    private LocalDate createdBy;
    @Column(
            name="deadline",
            nullable = false
    )
    private LocalDate deadline;


    // Constructors, getters, and setters

    // Define the Category enum
    public enum Category {
        COMPETITION,
        VOLUNTEER
    }
    public Event() {
        // Default constructor body
    }
    // Constructors
    public Event(Long id, String title, String organizationName, String description, Category category, String location,
                 String poster, String registerUrl, int views, LocalDate createdBy, LocalDate deadline) {
        this.title = title;
        this.organizationName = organizationName;
        this.description = description;
        this.category = category;
        this.location = location;
        this.poster = poster;
        this.registerUrl = registerUrl;
        this.views = views;
        this.createdBy = createdBy;
        this.deadline = deadline;
    }

    // Getters and Setters

    public Long getId() {
        return id != null ? id : 0L;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDate getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(LocalDate createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
