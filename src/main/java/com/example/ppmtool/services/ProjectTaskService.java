package com.example.ppmtool.services;

import com.example.ppmtool.domain.Backlog;
import com.example.ppmtool.domain.Project;
import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.exceptions.NotFoundException;
import com.example.ppmtool.repositories.BacklogRepository;
import com.example.ppmtool.repositories.ProjectRepository;
import com.example.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

        Backlog backlog = projectService.findProjectByIdentifierAndUsername(projectIdentifier, username).getBacklog();

        projectTask.setBacklog(backlog);
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);
        backlogRepository.save(backlog);
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence );
        projectTask.setProjectIdentifier(projectIdentifier);
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0 ) {
            projectTask.setPriority(3);
        }
        if (projectTask.getStatus() == null || projectTask.getStatus().equals("")) {
            projectTask.setStatus("TO DO");
        }
        return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogById(String backlogId, String username) {
        projectService.findProjectByIdentifierAndUsername(backlogId, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findByProjectSequence(String backlogId, String sequence, String username) {
        projectService.findProjectByIdentifierAndUsername(backlogId, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequenceAndProjectIdentifier(sequence, backlogId);
        return projectTask;
    }

    public ProjectTask updateProjectTask(ProjectTask updatedProjectTask, String backlogId, String sequence, String username) {
        projectService.findProjectByIdentifierAndUsername(backlogId, username);
        ProjectTask projectTask = projectTaskRepository.findByProjectSequenceAndProjectIdentifier(sequence, backlogId);
        if (projectTask == null ){
            throw new NotFoundException("Task is not found");
        }
        updatedProjectTask.setId(projectTask.getId());
        updatedProjectTask.setProjectIdentifier(backlogId);
        updatedProjectTask.setProjectSequence(sequence);
        projectTaskRepository.save(updatedProjectTask);
        return updatedProjectTask;

    }

    public void deleteProjectTask(String backlogId, String sequence, String username) {
        ProjectTask projectTask = findByProjectSequence(backlogId, sequence, username);
        projectTaskRepository.deleteById(projectTask.getId());
    }
}
