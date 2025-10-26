package com.zgdev.jirapro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Objet de transfert de données de statut de tâche")
public record TaskStatusDTO(
        @Schema(description = "Identifiant unique du statut de tâche", example = "1")
        long id,
        @Schema(description = "Nom du statut", example = "TO_DO")
        @NotBlank(message = "Status cannot be blank")
        @Length(min = 2, max = 50, message = "Status must be between 2 and 50 characters")
        String status
) {
}
