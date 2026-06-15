package com.embabel.framework.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String summary;

    @Column(name = "total_years_exp")
    private int totalYearsExp;

    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private List<Skill> skills;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private List<Project> projects;

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSummary() { return summary; }
    public int getTotalYearsExp() { return totalYearsExp; }
    public String getLocation() { return location; }
    public List<WorkExperience> getWorkExperiences() { return workExperiences; }
    public List<Skill> getSkills() { return skills; }
    public List<Project> getProjects() { return projects; }
}