package com.example.ppmtool.web;

import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.services.MapValidationErrorService;
import com.example.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, @PathVariable String backlogId, BindingResult result) {

        ResponseEntity<?> errorMap = mapValidationErrorService.validateRequest(result);

        return errorMap == null ? new ResponseEntity<>(projectTaskService.addProjectTask(backlogId, projectTask), HttpStatus.CREATED)
                : new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);

    }
}
