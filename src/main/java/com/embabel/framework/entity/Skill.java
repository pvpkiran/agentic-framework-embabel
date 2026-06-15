package com.embabel.framework.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "skill_name")
    private String skillName;

    private String category;
    private String proficiency;

    @Column(name = "years_of_exp")
    private int yearsOfExp;

    // Getters
    public Integer getId() { return id; }
    public String getSkillName() { return skillName; }
    public String getCategory() { return category; }
    public String getProficiency() { return proficiency; }
    public int getYearsOfExp() { return yearsOfExp; }
}
