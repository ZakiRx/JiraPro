package com.zgdev.jirapro.controller;

import com.zgdev.jirapro.dto.ErrorResponse;
import com.zgdev.jirapro.dto.SimpleResponseMessage;
import com.zgdev.jirapro.dto.TaskDTO;
import com.zgdev.jirapro.facade.TaskFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/tasks")
@Tag(name = "Gestion des Tâches", description = "APIs pour gérer les tâches")
public class TaskController {

    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @Operation(summary = "Obtenir toutes les tâches", description = "Récupérer la liste de toutes les tâches")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)))
    })
    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasks(){
        return  new ResponseEntity<>(this.taskFacade.getTasks(),  HttpStatus.OK);
    }

    @Operation(summary = "Obtenir les tâches par statut", description = "Récupérer les tâches filtrées par statut")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tâches récupérées avec succès par statut",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class)))
    })
    @GetMapping("/{status}")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(
            @Parameter(description = "Statut pour filtrer les tâches", example = "TO_DO")
            @PathVariable String status){
        return  new ResponseEntity<>(this.taskFacade.getTasksByStatus(status),  HttpStatus.OK);
    }

    @Operation(summary = "Créer une nouvelle tâche", description = "Créer une nouvelle tâche avec les détails fournis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tâche créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails de la tâche à créer",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TaskDTO.class)))
            @RequestBody TaskDTO taskDTO){
        return  new ResponseEntity<>(this.taskFacade.createTask(taskDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour une tâche", description = "Mettre à jour une tâche existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tâche mise à jour avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tâche non trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "ID de la tâche à mettre à jour", example = "1")
            @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Détails mis à jour de la tâche",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TaskDTO.class)))
            @RequestBody TaskDTO taskDTO){
        return  ResponseEntity.ok(this.taskFacade.createTask(taskDTO));
    }

    @Operation(summary = "Supprimer une tâche", description = "Supprimer une tâche par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tâche supprimée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleResponseMessage.class))),
            @ApiResponse(responseCode = "404", description = "Tâche non trouvée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleResponseMessage> deleteTask(
            @Parameter(description = "ID de la tâche à supprimer", example = "1")
            @PathVariable long id){
        this.taskFacade.deleteTask(id);
        return ResponseEntity.ok(new SimpleResponseMessage("Task has been removed","Task with id " + id + " has been removed"));
    }
}
