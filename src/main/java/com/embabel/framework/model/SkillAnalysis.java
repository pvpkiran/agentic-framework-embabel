package com.embabel.framework.model;

import java.util.List;

/**
 * Intermediate result: extracted skills comparison.
 * The LLM produces this typed object, so it's validated at compile time.
 */
public record SkillAnalysis(
        List<String> requiredSkills,
        List<String> candidateSkills,
        List<String> matchingSkills,
        List<String> missingSkills,
        int matchPercentage
) {}
