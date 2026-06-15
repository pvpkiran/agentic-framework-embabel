package com.embabel.framework.agent;

import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.core.CoreToolGroups;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.framework.model.*;
import com.embabel.framework.tool.CandidateLookupTools;

@Agent(description = "Prepares candidates for job interviews by combining their profile data with live company research")
public class InterviewCoachAgent {

    private final CandidateLookupTools candidateLookupTools;

    public InterviewCoachAgent(CandidateLookupTools candidateLookupTools) {
        this.candidateLookupTools = candidateLookupTools;
    }

    @Action
    public InterviewQuery parseQuery(UserInput input, OperationContext context) {
        return context.ai().withDefaultLlm()
                .createObject(
                        """
                        Extract the candidate name, company name, and target role from this request.
                        If any field is not mentioned, set it to "not specified".
                        
                        Request: %s
                        """.formatted(input),
                        InterviewQuery.class
                );
    }

    @Action
    public CandidateProfile fetchCandidateProfile(InterviewQuery query, OperationContext context) {
        return context.ai().withDefaultLlm()
                .withToolObject(candidateLookupTools)
                .createObject(
                        """
                        Look up the candidate "%s" in the database and build their profile.
                        
                        Steps:
                        1. Use findCandidate to get their ID and summary
                        2. Use getSkills with category "all" to get their full skill set
                        3. Use getWorkExperience with technology "all" to get their work history
                        4. Use getProjects with technology "all" to get their notable projects
                        
                        The target role is: %s
                        Highlight skills and experience most relevant to this role.
                        Return ONLY valid JSON, no preamble or explanation.
                        """.formatted(query.candidateName(), query.targetRole()),
                        CandidateProfile.class
                );
    }

    @Action
    public CompanyProfile researchCompany(InterviewQuery query, OperationContext context) {
        return context.ai().withDefaultLlm()
                .withToolGroup(CoreToolGroups.WEB)
                .createObject(
                        """
                        Research "%s" using web search. Limit to 3 searches max.
                        Return a concise company profile.
                        
                        Find:
                        - Company headquarters and industry
                        - When it was founded
                        - Their mission statement or core values
                        - Main products and services
                        - Tech stack they use (programming languages, frameworks, cloud)
                        - Recent news (last 6 months)
                        - Engineering culture — blog posts, talks, open source
                        
                        Use multiple searches to build a comprehensive picture.
                        Return ONLY valid JSON, no preamble or explanation.
                        """.formatted(query.companyName()),
                        CompanyProfile.class
                );
    }

    @AchievesGoal(description = "Produce a personalized interview preparation brief")
    @Action
    public InterviewBrief generateBrief(
            CandidateProfile candidate,
            CompanyProfile company,
            InterviewQuery query,
            OperationContext context) {
        return context.ai().withDefaultLlm()
                .createObject(
                        """
                        Based on this candidate's profile and company research, create a personalized interview preparation brief.
                        
                        Candidate: %s
                        Target Role: %s at %s
                        
                        Candidate Profile:
                        - Location: %s
                        - Experience: %d years
                        - Summary: %s
                        - Key Skills: %s
                        - Relevant Experience: %s
                        - Notable Projects: %s
                        
                        Company: %s
                        Industry: %s
                        Products: %s
                        Tech Stack: %s
                        Recent News: %s
                        Engineering Culture: %s
                        
                        Generate:
                        - A 2-3 sentence company summary
                        - 3-4 compelling "Why I want to work here" talking points specific to THIS candidate
                        - 4-5 smart questions to ask the interviewer
                        - 2-3 challenges the company might be facing
                        - 3-4 points on how THIS candidate's specific experience fits the role (reference actual projects and skills)
                        - A personalized 30-second opening pitch using the candidate's real background
                        """.formatted(
                                candidate.name(),
                                query.targetRole(), query.companyName(),
                                candidate.location(),
                                candidate.totalYearsExperience(),
                                candidate.summary(),
                                String.join(", ", candidate.keySkills()),
                                String.join("; ", candidate.relevantExperience()),
                                String.join("; ", candidate.notableProjects()),
                                company.companyName(),
                                company.industry(),
                                String.join(", ", company.products()),
                                String.join(", ", company.techStack()),
                                String.join("; ", company.recentNews()),
                                company.engineeringCulture()
                        ),
                        InterviewBrief.class
                );
    }
}