package com.embabel.agent.agent;

import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.model.FitReport;
import com.embabel.agent.model.JobApplication;
import com.embabel.agent.model.SkillAnalysis;

@Agent(description = "Reviews a job application by analyzing skill fit between a job description and a resume")
public class JobApplicationReviewerAgent {

    @Action
    public JobApplication parseApplication(UserInput userInput, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject("""
                        Parse the following input and extract:
                        - The job title
                        - The job description/requirements
                        - The candidate's resume/experience

                        If any part is missing, infer reasonable defaults.

                        Input: %s
                        """.formatted(userInput.getContent()),
                        JobApplication.class);
    }

    @Action
    public SkillAnalysis analyzeSkills(JobApplication application, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject("""
                        Compare the skills required for this job with the candidate's skills.

                        Job Title: %s
                        Job Description: %s
                        Candidate Resume: %s

                        Extract:
                        - Required skills from the job description
                        - Skills the candidate has
                        - Matching skills (intersection)
                        - Missing skills (required but candidate lacks)
                        - Match percentage (0-100)
                        """.formatted(
                                application.jobTitle(),
                                application.jobDescription(),
                                application.candidateResume()),
                        SkillAnalysis.class);
    }

    @AchievesGoal(description = "Produce a comprehensive job fit assessment")
    @Action
    public FitReport generateFitReport(
            SkillAnalysis analysis,
            JobApplication application,
            OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject("""
                        Based on this skill analysis, create a comprehensive fit report.

                        Job Title: %s
                        Match Percentage: %d%%
                        Matching Skills: %s
                        Missing Skills: %s

                        Provide:
                        - Overall fit score (0-100)
                        - A recommendation (Strong Fit / Moderate Fit / Weak Fit)
                        - Key strengths the candidate brings
                        - Gaps that need addressing
                        - A suggested angle for a cover letter that emphasizes strengths
                          and addresses gaps honestly
                        """.formatted(
                                application.jobTitle(),
                                analysis.matchPercentage(),
                                String.join(", ", analysis.matchingSkills()),
                                String.join(", ", analysis.missingSkills())),
                        FitReport.class);
    }
}
