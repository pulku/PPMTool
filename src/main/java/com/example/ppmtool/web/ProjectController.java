package com.example.ppmtool.web;

import com.example.ppmtool.domain.Project;
import com.example.ppmtool.services.MapValidationErrorService;
import com.example.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
        ResponseEntity<?> errorMap = validationErrorService.validateRequest(result);
        return errorMap == null ? new ResponseEntity<>(projectService.saveOrUpdateProject(project), HttpStatus.CREATED) : errorMap;
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> findProjectById(@PathVariable String projectIdentifier) {
        Project project = projectService.findProjectByIdentifier(projectIdentifier.toUpperCase());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> findAllProject() {
        List<Project> allProjects = projectService.findAll();
        if (allProjects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allProjects, HttpStatus.OK);
    }

    @DeleteMapping("/{projectIdentifier}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectIdentifier) {
        projectService.deleteProjectById(projectIdentifier.toUpperCase());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{projectIdentifier}")
    public ResponseEntity<?> updateProjectById(@PathVariable String projectIdentifier, @RequestBody Project project) {
        Project retrieved = projectService.findProjectByIdentifier(projectIdentifier.toUpperCase());
        project.setId(retrieved.getId());
        project.setCreatedAt(retrieved.getCreatedAt());
        return new ResponseEntity<>(projectService.saveOrUpdateProject(project), HttpStatus.OK);
    }
}
