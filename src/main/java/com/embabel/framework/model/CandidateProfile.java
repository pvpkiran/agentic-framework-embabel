package com.embabel.framework.model;

import java.util.List;

public record CandidateProfile(
        String name,
        String location,
        int totalYearsExperience,
        String summary,
        List<String> keySkills,
        List<String> relevantExperience,
        List<String> notableProjects
) {}
