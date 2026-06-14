package com.embabel.agent.model;

import java.util.List;

public record InterviewBrief(
        String companyName,
        String companySummary,
        List<String> whyThisCompany,
        List<String> questionsToAsk,
        List<String> potentialChallenges,
        List<String> howYourExperienceFits,
        String openingPitch
) {}