package com.example.ppmtool.web;

import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.exceptions.NotFoundExceptionResponse;
import com.example.ppmtool.services.MapValidationErrorService;
import com.example.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}/tasks")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlogId, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.validateRequest(result);

        return errorMap == null ? new ResponseEntity<>(projectTaskService.addProjectTask(backlogId, projectTask, principal.getName()), HttpStatus.CREATED)
                : errorMap;

    }

    @GetMapping("/{backlogId}/tasks")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String backlogId, Principal principal) {

        Iterable<ProjectTask> backlogById = projectTaskService.findBacklogById(backlogId, principal.getName());
        return new ResponseEntity<>(backlogById, backlogById.iterator().hasNext() ? HttpStatus.OK : HttpStatus.NO_CONTENT );

    }

    @GetMapping("/{backlogId}/tasks/{taskSequence}")
    public ResponseEntity<?> getTaskBySequence(@PathVariable String backlogId, @PathVariable String taskSequence, Principal principal) {
        return new ResponseEntity<>(projectTaskService.findByProjectSequence(backlogId, taskSequence, principal.getName()), HttpStatus.OK );
    }

    @PutMapping("/{backlogId}/tasks/{taskSequence}")
    public ResponseEntity<?> updateTask(@PathVariable String backlogId, @PathVariable String taskSequence, @Valid @RequestBody ProjectTask projectTask, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapValidationErrorService.validateRequest(result);

        return errorMap == null ? new ResponseEntity<>(projectTaskService.updateProjectTask(projectTask, backlogId, taskSequence, principal.getName()), HttpStatus.OK)
                : errorMap;
    }

    @DeleteMapping("/{backlogId}/tasks/{taskSequence}")
    public ResponseEntity<?> deleteTask(@PathVariable String backlogId, @PathVariable String taskSequence, Principal principal) {
        projectTaskService.deleteProjectTask(backlogId, taskSequence, principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
