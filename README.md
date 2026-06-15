# Embabel Agentic Framework

A hands-on exploration of the [Embabel](https://github.com/embabel/embabel-agent) agentic AI framework (v0.4.0). This project contains three agents that demonstrate GOAP-based planning, typed domain models, LLM-powered actions, MCP tool integration with Brave Search, and `@LlmTool` database access via Spring Data JPA.

Embabel is a JVM-based agentic framework by Rod Johnson (creator of Spring Framework). It sits on top of Spring AI and uses Goal-Oriented Action Planning (GOAP) to automatically sequence actions based on Java type signatures — you define what each action consumes and produces, and the planner figures out the execution order.

## Agents

### 1. Job application reviewer (no external tools)

Takes a free-text job application (resume + job description) and produces a fit report.

**GOAP plan:** `UserInput → JobApplication → SkillAnalysis → FitReport`

Each step is an `@Action` method that takes a typed input and returns a typed output. The planner infers the chain from the type signatures automatically.

### 2. Company research agent (with Brave Search MCP)

Takes a natural language query like "Research Personio for my Senior Java Developer interview" and produces a structured interview preparation brief using live web search.

**GOAP plan:** `UserInput → CompanyQuery → CompanyProfile → InterviewBrief`

The `researchCompany` action uses `.withToolGroup(CoreToolGroups.WEB)` to give the LLM access to Brave Search tools during its reasoning. The LLM decides when and what to search within a tool loop — Embabel orchestrates the back-and-forth between the LLM and the MCP server.

### 3. Interview coach agent (Brave Search MCP + PostgreSQL via @LlmTool)

Takes a candidate name and target company, pulls the candidate's profile from a PostgreSQL database, researches the company live via web search, and produces a **personalized** interview brief that maps the candidate's actual skills and projects to the company's needs.

**GOAP plan:** `UserInput → InterviewQuery → CandidateProfile → CompanyProfile → InterviewBrief`

This agent combines two tool sources in a single pipeline:
- **`@LlmTool` methods** — Claude calls Java methods annotated with `@LlmTool` to query the PostgreSQL database (find candidate, get skills, get work experience, get projects). These run in the JVM, not via MCP.
- **Brave MCP tools** — Claude uses web search to research the target company, same as the Company Research Agent.

The final `generateBrief` action receives both the candidate profile and company research, producing talking points that reference the candidate's actual projects and experience.

## Project structure

```
src/main/java/com/embabel/framework/
├── EmbabelApplication.java                # @SpringBootApplication + @EnableAgents
├── agent/
│   ├── JobApplicationReviewerAgent.java   # 3 actions, no tools
│   ├── CompanyResearchAgent.java          # 3 actions, Brave MCP
│   └── InterviewCoachAgent.java           # 4 actions, Brave MCP + @LlmTool DB
├── config/
│   └── WebToolGroupConfig.java            # Maps MCP tools to Embabel's "web" role
├── entity/
│   ├── Candidate.java                     # JPA entity: name, summary, experience
│   ├── WorkExperience.java                # JPA entity: company, role, technologies
│   ├── Skill.java                         # JPA entity: skill name, proficiency, years
│   └── Project.java                       # JPA entity: project name, impact, technologies
├── model/
│   ├── JobApplication.java                # record: candidateName, skills, experience...
│   ├── SkillAnalysis.java                 # record: matchedSkills, gaps, score...
│   ├── FitReport.java                     # record: recommendation, summary...
│   ├── CompanyQuery.java                  # record: companyName, targetRole
│   ├── CompanyProfile.java                # record: techStack, products, recentNews...
│   ├── InterviewBrief.java                # record: openingPitch, questionsToAsk...
│   ├── InterviewQuery.java                # record: candidateName, companyName, targetRole
│   └── CandidateProfile.java             # record: keySkills, relevantExperience, projects
├── repository/
│   ├── CandidateRepository.java           # findByNameIgnoreCase
│   ├── SkillRepository.java               # findByCandidateIdAndCategory
│   ├── WorkExperienceRepository.java      # findByTechnology (LIKE query)
│   └── ProjectRepository.java             # findByTechnology (LIKE query)
└── tool/
    └── CandidateLookupTools.java          # @LlmTool methods wrapping repositories
```

## Prerequisites

- Java 21+ (project uses Java records and virtual threads)
- Maven 3.9+
- Docker (for PostgreSQL)
- Node.js 18+ and npm (for the Brave MCP server, which runs as an npx child process)
- Anthropic API key (free tier works, but has rate limits)
- Brave Search API key (free tier: 2,000 queries/month — get one at https://brave.com/search/api/)

## Setup

### 1. Get API keys

**Anthropic:** Sign up at https://console.anthropic.com and create an API key.

**Brave Search:** Sign up at https://brave.com/search/api/, create a free plan, and copy the API key.

### 2. Set environment variables

Add these to your `~/.zshrc` (or `~/.bashrc`):

```bash
export ANTHROPIC_API_KEY=sk-ant-...
export BRAVE_API_KEY=BSA...
```

Then reload: `source ~/.zshrc`

### 3. Start PostgreSQL

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container with the `interview_coach` database, seeded with 4 candidate profiles (Marcus Weber, Lena Kovacs, Raj Mehta, Sophie Brenner) with varied skills, work history, and projects.

Verify the database is running:

```bash
docker exec -it interview-coach-db psql -U coach -d interview_coach -c "SELECT id, name, total_years_exp, location FROM candidate;"
```

### 4. Verify npx path

The MCP config needs the full path to `npx`. Find it with:

```bash
which npx
```

If it's not `/usr/local/bin/npx`, update the `command` field in `application.yml`.

### 5. Build and run

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

You should see the Embabel banner, followed by:

```
MCP server starting.
MCP server started
Found 1 clients: Implementation[name=brave-search-mcp-server, ...]
Deployed agent CompanyResearchAgent
Deployed agent InterviewCoachAgent
Deployed agent JobApplicationReviewerAgent
Current LLM settings: maxAttempts=3, fixedBackoffMillis=30ms, timeout=120s
```

## Usage

The app starts an interactive shell. Use the `x` command to run queries:

### Interview coaching (with DB + web search)

```
embabel> x "Prepare Marcus Weber for a Senior Java Developer interview at Personio"
embabel> x "Coach Lena Kovacs for a Full-Stack Engineer interview at Delivery Hero"
embabel> x "Help Raj Mehta prepare for a Data Engineer role at BMW"
embabel> x "Prepare Sophie Brenner for a Platform Engineer interview at Celonis"
```

This triggers a 4-step GOAP pipeline: parse query → fetch candidate from DB (via `@LlmTool`) → research company (via Brave MCP) → generate personalized brief. Takes about 60-90 seconds. The output references the candidate's actual projects and maps them to the company's needs.

### Company research (with web search)

```
embabel> x "Research <COMPANY NAME> for my Senior Java Developer interview"
```

This triggers the full GOAP pipeline: parse query → web research (3 Brave searches) → generate interview brief. Takes about 60-90 seconds. Output includes company summary, tech stack, talking points, questions to ask, and an opening pitch.

### Job application review

```
embabel> x "Job: Senior Java Developer at a fintech. Requirements: Java 17+, Spring Boot, microservices, Kafka, PostgreSQL. Candidate: 10 years Java experience, Spring Boot expert, worked on payment systems at Visa, knows Kafka and Redis, no PostgreSQL experience."
```

### Other shell commands

```
embabel> tools          # List registered tool groups (should show: AppleScript, math, web)
embabel> agents         # List deployed agents
```

## Architecture

![Architecture](architecture.png)

## Rate limiting notes

On Anthropic's free tier, the company research agent can hit rate limits if the LLM makes too many searches. Three configuration changes address this:

- **Prompt constraint** ("Limit to 3 searches max") — reduces the number of tool loop iterations, which reduces API calls. This is the most impactful fix.
- **`max-attempts: 3`** (down from default 10) — limits retry cascading. Each retry restarts the full tool loop, so 10 retries × 4 iterations = 40+ API calls competing for the same rate limit window.
- **`default-timeout: 120s`** (up from default 60s) — prevents premature timeouts from triggering unnecessary retries.


## Database management

To wipe the database and re-seed from scratch:

```bash
docker compose down -v
docker compose up -d
```

The `-v` flag deletes the named volume, so PostgreSQL runs `init.sql` again on next startup.