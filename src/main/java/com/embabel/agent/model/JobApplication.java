package com.embabel.agent.model;

import java.util.List;

/**
 * Domain model for the Job Application Reviewer Agent.
 *
 * These Java records form the TYPED DOMAIN MODEL that Embabel uses
 * for its GOAP planning. The framework inspects input/output types
 * of each @Action to determine the execution order automatically.
 *
 * Flow: UserInput → JobApplication → SkillAnalysis → FitReport (goal achieved)
 */

// ── Input: Raw user input containing job description + resume ──
public record JobApplication(
        String jobTitle,
        String jobDescription,
        String candidateResume
) {}
