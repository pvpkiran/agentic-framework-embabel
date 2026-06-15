package com.embabel.framework.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "project_name")
    private String projectName;

    private String description;
    private String technologies;
    private String impact;

    // Getters
    public Integer getId() { return id; }
    public String getProjectName() { return projectName; }
    public String getDescription() { return description; }
    public String getTechnologies() { return technologies; }
    public String getImpact() { return impact; }
}