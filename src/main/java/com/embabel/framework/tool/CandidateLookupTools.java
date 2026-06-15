package com.embabel.framework.tool;

import com.embabel.agent.api.annotation.LlmTool;
import com.embabel.framework.entity.Project;
import com.embabel.framework.entity.Skill;
import com.embabel.framework.entity.WorkExperience;
import com.embabel.framework.repository.CandidateRepository;
import com.embabel.framework.repository.ProjectRepository;
import com.embabel.framework.repository.SkillRepository;
import com.embabel.framework.repository.WorkExperienceRepository;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CandidateLookupTools {

    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final ProjectRepository projectRepository;

    public CandidateLookupTools(
            CandidateRepository candidateRepository,
            SkillRepository skillRepository,
            WorkExperienceRepository workExperienceRepository,
            ProjectRepository projectRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.projectRepository = projectRepository;
    }

    @LlmTool(description = "Look up a candidate by name. Returns their ID, summary, years of experience, and location. Use this first to get the candidate ID needed by other tools.")
    public String findCandidate(
            @ToolParam(description = "Full or partial name of the candidate") String name
    ) {
        return candidateRepository.findByNameIgnoreCase(name)
                .map(c -> String.format(
                        "ID: %d\nName: %s\nLocation: %s\nYears of experience: %d\nSummary: %s",
                        c.getId(), c.getName(), c.getLocation(), c.getTotalYearsExp(), c.getSummary()))
                .orElse("No candidate found with name: " + name);
    }

    @LlmTool(description = "Get a candidate's skills, optionally filtered by category. Categories include: language, framework, database, cloud, infrastructure, messaging, architecture, devops, frontend, ml, observability, security, api, data")
    public String getSkills(
            @ToolParam(description = "Candidate ID (get this from findCandidate first)") Integer candidateId,
            @ToolParam(description = "Skill category to filter by, or 'all' for everything") String category
    ) {
        List<Skill> skills = category.equalsIgnoreCase("all")
                ? skillRepository.findByCandidateId(candidateId)
                : skillRepository.findByCandidateIdAndCategoryIgnoreCase(candidateId, category);

        if (skills.isEmpty()) {
            return "No skills found for candidate " + candidateId + " in category: " + category;
        }

        return skills.stream()
                .map(s -> String.format("%s (%s) — %s, %d years",
                        s.getSkillName(), s.getCategory(), s.getProficiency(), s.getYearsOfExp()))
                .collect(Collectors.joining("\n"));
    }

    @LlmTool(description = "Get a candidate's work experience. Can filter by a specific technology to find relevant roles.")
    public String getWorkExperience(
            @ToolParam(description = "Candidate ID") Integer candidateId,
            @ToolParam(description = "Technology to filter by, or 'all' for full work history") String technology
    ) {
        List<WorkExperience> experiences = technology.equalsIgnoreCase("all")
                ? workExperienceRepository.findByCandidateIdOrderByStartDateDesc(candidateId)
                : workExperienceRepository.findByTechnology(candidateId, technology);

        if (experiences.isEmpty()) {
            return "No work experience found for candidate " + candidateId
                    + (technology.equalsIgnoreCase("all") ? "" : " with technology: " + technology);
        }

        return experiences.stream()
                .map(w -> String.format("%s at %s (%s to %s)\n  %s\n  Technologies: %s",
                        w.getRole(), w.getCompany(),
                        w.getStartDate(),
                        w.getEndDate() != null ? w.getEndDate().toString() : "present",
                        w.getDescription(),
                        w.getTechnologies()))
                .collect(Collectors.joining("\n\n"));
    }

    @LlmTool(description = "Get a candidate's notable projects. Can filter by technology to find relevant projects.")
    public String getProjects(
            @ToolParam(description = "Candidate ID") Integer candidateId,
            @ToolParam(description = "Technology to filter by, or 'all' for all projects") String technology
    ) {
        List<Project> projects = technology.equalsIgnoreCase("all")
                ? projectRepository.findByCandidateId(candidateId)
                : projectRepository.findByTechnology(candidateId, technology);

        if (projects.isEmpty()) {
            return "No projects found for candidate " + candidateId
                    + (technology.equalsIgnoreCase("all") ? "" : " with technology: " + technology);
        }

        return projects.stream()
                .map(p -> String.format("%s\n  %s\n  Technologies: %s\n  Impact: %s",
                        p.getProjectName(), p.getDescription(), p.getTechnologies(), p.getImpact()))
                .collect(Collectors.joining("\n\n"));
    }
}