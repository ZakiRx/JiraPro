package com.zgdev.jirapro.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objet de transfert de données de statut de tâche")
public record TaskStatusDTO(
        @Schema(description = "Identifiant unique du statut de tâche", example = "1")
        long id,
        @Schema(description = "Nom du statut", example = "TO_DO", allowableValues = {"TO_DO", "IN_PROGRESS", "DONE"})
        String status
) {
}
