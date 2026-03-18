package com.satyam.fintrack.exceptions;



import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private List<String> messages;
    private LocalDateTime timestamp;

}
