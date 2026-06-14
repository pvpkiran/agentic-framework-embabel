package com.embabel.agent.model;

public record CompanyQuery(
        String companyName,
        String targetRole,
        String specificInterests
) {}