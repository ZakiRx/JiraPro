package com.zgdev.jirapro.controller;

import com.zgdev.jirapro.dto.ErrorResponse;
import com.zgdev.jirapro.dto.TaskStatusDTO;
import com.zgdev.jirapro.facade.TaskStatusFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/task-statuses")
@Tag(name = "Gestion des Statuts de Tâches", description = "APIs pour gérer les statuts des tâches")
public class TaskStatusController {

    private final TaskStatusFacade taskStatusFacade;

    public TaskStatusController(TaskStatusFacade taskFacade) {
        this.taskStatusFacade = taskFacade;
    }

    @Operation(summary = "Obtenir tous les statuts de tâches", description = "Récupérer la liste de tous les statuts de tâches disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des statuts de tâches récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskStatusDTO.class)))
    })
    @GetMapping()
    public ResponseEntity<List<TaskStatusDTO>> getTaskStatus(){
        return  new ResponseEntity<>(this.taskStatusFacade.getTaskStatus(),  HttpStatus.OK);
    }

    @Operation(summary = "Créer un nouveau statut de tâche", description = "Créer un nouveau statut de tâche avec les détails fournis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Statut de tâche créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskStatusDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<TaskStatusDTO> createTaskStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails du statut de tâche à créer",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TaskStatusDTO.class)))
            @RequestBody @Valid TaskStatusDTO taskDTO){
        return  new ResponseEntity<>(this.taskStatusFacade.createTaskStatus(taskDTO), HttpStatus.CREATED);
    }
}
