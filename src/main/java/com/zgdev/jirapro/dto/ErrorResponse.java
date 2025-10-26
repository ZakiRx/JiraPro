package com.zgdev.jirapro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Objet de réponse d'erreur")
public record ErrorResponse(
        @Schema(description = "Horodatage de l'erreur", example = "2025-01-15 10:30:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        @Schema(description = "Code de statut HTTP", example = "404")
        int status,
        @Schema(description = "Type d'erreur", example = "Tâche non trouvée")
        String error,
        @Schema(description = "Message d'erreurca détaillé", example = "Tâche avec l'id 123 non trouvée")
        String message
) {
    public ErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message);
    }
}
