package com.embabel.agent.agent;

import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.core.CoreToolGroups;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.model.CompanyProfile;
import com.embabel.agent.model.CompanyQuery;
import com.embabel.agent.model.InterviewBrief;

@Agent(description = "Researches companies using web search and prepares interview briefings")
public class CompanyResearchAgent {

    @Action
    public CompanyQuery parseQuery(UserInput userInput, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject("""
                        Extract the company name, target role (if mentioned),
                        and any specific interests from this request.
                        If role or interests aren't mentioned, set them to "not specified".

                        Request: %s
                        """.formatted(userInput),
                        CompanyQuery.class);
    }

    @Action
    public CompanyProfile researchCompany(CompanyQuery query, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .withToolGroup(CoreToolGroups.WEB)
                .createObject("""
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
                        """.formatted(query.companyName()),
                        CompanyProfile.class);
    }

    @AchievesGoal(description = "Produce a tailored interview preparation briefing")
    @Action
    public InterviewBrief generateBrief(
            CompanyProfile profile,
            CompanyQuery query,
            OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject("""
                        Based on this company research, create an interview preparation brief.

                        Company: %s
                        Target Role: %s
                        Industry: %s
                        Products: %s
                        Tech Stack: %s
                        Recent News: %s
                        Engineering Culture: %s

                        Generate:
                        - A 2-3 sentence company summary
                        - 3-4 compelling "Why I want to work here" talking points
                        - 4-5 smart questions to ask the interviewer
                        - 2-3 challenges the company might be facing
                        - 3-4 points on how a senior Java/Spring developer fits
                        - A 30-second opening pitch
                        """.formatted(
                                profile.companyName(),
                                query.targetRole(),
                                profile.industry(),
                                String.join(", ", profile.products()),
                                String.join(", ", profile.techStack()),
                                String.join(", ", profile.recentNews()),
                                profile.engineeringCulture()),
                        InterviewBrief.class);
    }
}