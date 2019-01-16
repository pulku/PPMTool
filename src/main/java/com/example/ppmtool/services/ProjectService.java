package com.example.ppmtool.services;

import com.example.ppmtool.domain.Project;
import com.example.ppmtool.exceptions.NotFoundException;
import com.example.ppmtool.exceptions.ProjectIdException;
import com.example.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (DataIntegrityViolationException ex) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' is already exists");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier);
        if (project == null) {
            throw new NotFoundException("Project with id '" + projectIdentifier + "' not found");
        }
        return project;
    }

    public List<Project> findAll() {
        return (List<Project>) projectRepository.findAll();
    }

    public void deleteProjectById(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier);
        if (project == null) {
            throw new NotFoundException("Project with id '" + projectIdentifier + "' not found");
        }
        projectRepository.delete(project);
    }

}
