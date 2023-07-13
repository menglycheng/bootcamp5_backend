package com.checkme.CheckMe.event.dto;

import com.checkme.CheckMe.event.entity.Event;
import jakarta.mail.Multipart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    private String title;
    private String description;
    private Event.Category category;
    private MultipartFile poster;
    private String location;
    private String registerUrl;
    private LocalDate deadline;
}
