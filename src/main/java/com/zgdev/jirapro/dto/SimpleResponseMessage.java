package com.zgdev.jirapro.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Message de réponse simple")
public record SimpleResponseMessage(
        @Schema(description = "Titre de la réponse", example = "Succès")
        String title,
        @Schema(description = "Message de réponse", example = "Opération terminée avec succès")
        String message
) {
}
