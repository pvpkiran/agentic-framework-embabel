package com.embabel.agent.model;

import java.util.List;

public record CompanyProfile(
        String companyName,
        String headquarters,
        String industry,
        String founded,
        String missionStatement,
        List<String> products,
        List<String> techStack,
        List<String> recentNews,
        String engineeringCulture
) {}