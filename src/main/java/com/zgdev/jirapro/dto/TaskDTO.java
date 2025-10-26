package com.zgdev.jirapro.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objet de transfert de données de tâche")
public record TaskDTO(
        @Schema(description = "Identifiant unique de la tâche", example = "1")
        long id,
        @Schema(description = "Libellé/titre de la tâche", example = "Implémenter l'authentification utilisateur")
        String label,
        @Schema(description = "Description détaillée de la tâche", example = "Implémenter l'authentification JWT pour l'API")
        String description,
        @Schema(description = "Statut actuel de la tâche")
        TaskStatusDTO completed
) {
}
