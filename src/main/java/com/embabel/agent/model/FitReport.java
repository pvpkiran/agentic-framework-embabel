package com.embabel.agent.model;

import java.util.List;

/**
 * Final output: the comprehensive fit report.
 * This is the GOAL type - when the agent produces this, the goal is achieved.
 */
public record FitReport(
        String jobTitle,
        int overallFitScore,
        String recommendation,
        List<String> strengths,
        List<String> gaps,
        String suggestedCoverLetterAngle
) {}
