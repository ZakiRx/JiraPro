package com.zgdev.jirapro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Objet de transfert de données de tâche")
public record TaskDTO(
        @Schema(description = "Identifiant unique de la tâche", example = "1")
        long id,
        @Schema(description = "Libellé/titre de la tâche", example = "Implémenter l'authentification utilisateur")
        @NotBlank(message = "Label cannot be blank")
        @Length(min = 3, max = 100, message = "Label must be between 3 and 100 characters")
        String label,
        @Schema(description = "Description détaillée de la tâche", example = "Implémenter l'authentification JWT pour l'API")
        @Length(min = 10, max = 250, message = "Description must be between 10 and 250 characters")
        String description,
        @Schema(description = "Statut actuel de la tâche")
        @NotNull(message = "Task status cannot be null")
        @Valid
        TaskStatusDTO completed
) {
}
