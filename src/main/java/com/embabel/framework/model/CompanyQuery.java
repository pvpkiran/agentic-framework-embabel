package com.embabel.framework.model;

public record CompanyQuery(
        String companyName,
        String targetRole,
        String specificInterests
) {}