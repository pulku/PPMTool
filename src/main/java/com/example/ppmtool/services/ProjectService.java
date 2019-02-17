package com.example.ppmtool.services;

import com.example.ppmtool.domain.Backlog;
import com.example.ppmtool.domain.Project;
import com.example.ppmtool.domain.User;
import com.example.ppmtool.exceptions.NotFoundException;
import com.example.ppmtool.exceptions.ProjectIdException;
import com.example.ppmtool.repositories.BacklogRepository;
import com.example.ppmtool.repositories.ProjectRepository;
import com.example.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Project saveOrUpdateProject(Project project, String username) {

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new NotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new NotFoundException("Project is not found");
            }
        }
        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }
            return projectRepository.save(project);
        } catch (DataIntegrityViolationException ex) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' is already exists");
        }
    }

    public Project findProjectByIdentifierAndUsername(String projectIdentifier, String username) {
        Project project = projectRepository.findByProjectIdentifierAndProjectLeader(projectIdentifier, username);
        if (project == null) {
            throw new NotFoundException("Project with id '" + projectIdentifier + "' not found");
        }
        if (!project.getProjectLeader().equals(username)) {
            throw new NotFoundException("Project with id '" + projectIdentifier + "' not found in your account");
        }
        return project;
    }


    public List<Project> findAll(Principal principal) {
        return (List<Project>) projectRepository.findAllByProjectLeader(principal.getName());
    }

    public void deleteProjectById(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier);
        if (project == null) {
            throw new NotFoundException("Project with id '" + projectIdentifier + "' not found");
        }
        projectRepository.delete(project);
    }

}
