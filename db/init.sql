-- ============================================================
-- Interview Coach Database - Schema and Seed Data
-- ============================================================

-- ── Schema ──

CREATE TABLE candidate (
                           id              SERIAL PRIMARY KEY,
                           name            VARCHAR(100) NOT NULL,
                           email           VARCHAR(150) NOT NULL,
                           summary         TEXT NOT NULL,
                           total_years_exp INTEGER NOT NULL,
                           location        VARCHAR(100) NOT NULL,
                           created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE work_experience (
                                 id              SERIAL PRIMARY KEY,
                                 candidate_id    INTEGER NOT NULL REFERENCES candidate(id),
                                 company         VARCHAR(100) NOT NULL,
                                 role            VARCHAR(150) NOT NULL,
                                 start_date      DATE NOT NULL,
                                 end_date        DATE,
                                 description     TEXT NOT NULL,
                                 technologies    TEXT NOT NULL
);

CREATE TABLE skill (
                       id              SERIAL PRIMARY KEY,
                       candidate_id    INTEGER NOT NULL REFERENCES candidate(id),
                       skill_name      VARCHAR(100) NOT NULL,
                       category        VARCHAR(50) NOT NULL,
                       proficiency     VARCHAR(20) NOT NULL CHECK (proficiency IN ('beginner', 'intermediate', 'advanced', 'expert')),
                       years_of_exp    INTEGER NOT NULL
);

CREATE TABLE project (
                         id              SERIAL PRIMARY KEY,
                         candidate_id    INTEGER NOT NULL REFERENCES candidate(id),
                         project_name    VARCHAR(150) NOT NULL,
                         description     TEXT NOT NULL,
                         technologies    TEXT NOT NULL,
                         impact          TEXT NOT NULL
);

-- ── Seed Data ──

-- ============================================================
-- Candidate 1: Marcus Weber - Senior Java Backend Engineer
-- ============================================================

INSERT INTO candidate (id, name, email, summary, total_years_exp, location)
VALUES (1, 'Marcus Weber', 'marcus.weber@example.com',
        'Senior Java backend engineer with 14 years of experience building distributed systems and microservices. Deep expertise in Spring Boot, Kafka, and cloud-native architectures on AWS. Proven track record of leading platform migrations and mentoring engineering teams. Passionate about clean architecture, event-driven systems, and developer productivity.',
        14, 'Munich, Germany');

-- Work experience
INSERT INTO work_experience (candidate_id, company, role, start_date, end_date, description, technologies) VALUES
                                                                                                               (1, 'FinServe GmbH', 'Staff Engineer', '2021-03-01', NULL,
                                                                                                                'Leading the platform engineering team responsible for the core payment processing pipeline handling 2M+ transactions per day. Drove the migration from monolithic architecture to event-driven microservices. Designed and implemented a saga-based distributed transaction system for cross-border payments. Established SRE practices and reduced production incidents by 60%.',
                                                                                                                'Java 21, Spring Boot 3, Kafka, PostgreSQL, Redis, Kubernetes, AWS, Terraform, Datadog'),

                                                                                                               (1, 'TechCommerce AG', 'Senior Software Engineer', '2017-06-01', '2021-02-28',
                                                                                                                'Built and maintained the order management system serving 500K daily orders across 12 European markets. Implemented real-time inventory synchronization using Kafka Streams. Led the migration from Java 8 to Java 11 and Spring Boot 1.x to 2.x across 30+ microservices. Mentored 4 junior developers.',
                                                                                                                'Java 11, Spring Boot 2, Kafka Streams, MySQL, Elasticsearch, Docker, AWS ECS, Jenkins'),

                                                                                                               (1, 'DataBridge Solutions', 'Software Engineer', '2014-01-01', '2017-05-31',
                                                                                                                'Developed B2B integration platform connecting enterprise clients via REST and SOAP APIs. Built ETL pipelines processing 10GB+ daily data feeds. Introduced automated testing practices, increasing code coverage from 20% to 80%.',
                                                                                                                'Java 8, Spring MVC, Hibernate, Oracle DB, RabbitMQ, Maven, SVN'),

                                                                                                               (1, 'WebStart GmbH', 'Junior Developer', '2011-04-01', '2013-12-31',
                                                                                                                'Full-stack development on a Java-based CMS platform. Built custom reporting modules and client-facing dashboards. First exposure to agile practices and continuous integration.',
                                                                                                                'Java 7, JSP, jQuery, MySQL, Tomcat, Ant');

-- Skills
INSERT INTO skill (candidate_id, skill_name, category, proficiency, years_of_exp) VALUES
                                                                                      (1, 'Java', 'language', 'expert', 14),
                                                                                      (1, 'Spring Boot', 'framework', 'expert', 10),
                                                                                      (1, 'Kafka', 'messaging', 'expert', 7),
                                                                                      (1, 'PostgreSQL', 'database', 'expert', 8),
                                                                                      (1, 'Redis', 'database', 'advanced', 5),
                                                                                      (1, 'Kubernetes', 'infrastructure', 'advanced', 4),
                                                                                      (1, 'AWS', 'cloud', 'advanced', 6),
                                                                                      (1, 'Terraform', 'infrastructure', 'intermediate', 3),
                                                                                      (1, 'Docker', 'infrastructure', 'expert', 7),
                                                                                      (1, 'Elasticsearch', 'database', 'intermediate', 4),
                                                                                      (1, 'MySQL', 'database', 'advanced', 10),
                                                                                      (1, 'REST API Design', 'architecture', 'expert', 12),
                                                                                      (1, 'Microservices Architecture', 'architecture', 'expert', 7),
                                                                                      (1, 'Event-Driven Architecture', 'architecture', 'advanced', 5),
                                                                                      (1, 'CI/CD', 'devops', 'advanced', 8),
                                                                                      (1, 'Python', 'language', 'intermediate', 3),
                                                                                      (1, 'Kotlin', 'language', 'intermediate', 2);

-- Projects
INSERT INTO project (candidate_id, project_name, description, technologies, impact) VALUES
                                                                                        (1, 'Payment Gateway Modernization',
                                                                                         'Led end-to-end redesign of the payment processing pipeline from a monolithic Java EE application to an event-driven microservices architecture. Implemented saga pattern for distributed transactions with compensating actions for payment reversals.',
                                                                                         'Java 21, Spring Boot 3, Kafka, PostgreSQL, Redis, AWS EKS',
                                                                                         'Reduced payment processing latency by 70% (from 3s to 900ms). Enabled horizontal scaling, supporting 3x traffic growth without infrastructure changes.'),

                                                                                        (1, 'Real-Time Inventory System',
                                                                                         'Designed and built a real-time inventory synchronization system using Kafka Streams for a multi-market e-commerce platform. Implemented exactly-once semantics to prevent overselling across 12 warehouses.',
                                                                                         'Java 11, Kafka Streams, Spring Boot 2, Elasticsearch, Redis',
                                                                                         'Eliminated inventory discrepancies that were costing €2M annually. Reduced order cancellation rate from 4% to 0.3%.'),

                                                                                        (1, 'API Platform',
                                                                                         'Built a centralized API gateway and developer portal for B2B partners. Implemented rate limiting, API versioning, OAuth2 authentication, and automated SDK generation.',
                                                                                         'Java 11, Spring Cloud Gateway, OAuth2, OpenAPI, PostgreSQL',
                                                                                         'Onboarded 40+ B2B partners in the first year. Reduced partner integration time from 6 weeks to 3 days.');


-- ============================================================
-- Candidate 2: Lena Kovacs - Full-Stack Engineer
-- ============================================================

INSERT INTO candidate (id, name, email, summary, total_years_exp, location)
VALUES (2, 'Lena Kovacs', 'lena.kovacs@example.com',
        'Full-stack engineer with 9 years of experience across React, Node.js, and Java ecosystems. Specializes in building product-facing applications with strong UX sensibility. Experienced in migrating legacy frontends to modern SPA architectures and implementing design systems at scale. Comfortable working across the stack from database design to pixel-perfect UI.',
        9, 'Berlin, Germany');

-- Work experience
INSERT INTO work_experience (candidate_id, company, role, start_date, end_date, description, technologies) VALUES
                                                                                                               (2, 'ProductHub GmbH', 'Senior Full-Stack Engineer', '2022-01-01', NULL,
                                                                                                                'Technical lead for the customer-facing dashboard serving 200K monthly active users. Rebuilt the frontend from AngularJS to React with TypeScript, improving page load times by 50%. Designed and implemented a GraphQL API layer replacing 40+ REST endpoints. Introduced component-driven development with Storybook.',
                                                                                                                'React, TypeScript, GraphQL, Node.js, PostgreSQL, AWS Lambda, Storybook, Tailwind CSS'),

                                                                                                               (2, 'HealthTrack AG', 'Full-Stack Developer', '2019-03-01', '2021-12-31',
                                                                                                                'Built patient management features for a SaaS health-tech platform used by 500+ clinics. Implemented FHIR-compliant API integrations with hospital systems. Led accessibility initiative achieving WCAG 2.1 AA compliance across the application.',
                                                                                                                'React, Java 11, Spring Boot 2, PostgreSQL, Docker, REST, FHIR'),

                                                                                                               (2, 'DigitalCraft Studio', 'Frontend Developer', '2016-06-01', '2019-02-28',
                                                                                                                'Developed interactive web applications for agency clients across finance, automotive, and retail sectors. Built a reusable component library used across 15+ client projects. Introduced automated visual regression testing.',
                                                                                                                'JavaScript, React, Angular, SASS, Node.js, Express, MongoDB, Jest');

-- Skills
INSERT INTO skill (candidate_id, skill_name, category, proficiency, years_of_exp) VALUES
                                                                                      (2, 'JavaScript', 'language', 'expert', 9),
                                                                                      (2, 'TypeScript', 'language', 'expert', 6),
                                                                                      (2, 'React', 'framework', 'expert', 7),
                                                                                      (2, 'Node.js', 'framework', 'advanced', 6),
                                                                                      (2, 'Java', 'language', 'intermediate', 3),
                                                                                      (2, 'Spring Boot', 'framework', 'intermediate', 3),
                                                                                      (2, 'GraphQL', 'api', 'advanced', 4),
                                                                                      (2, 'PostgreSQL', 'database', 'advanced', 5),
                                                                                      (2, 'MongoDB', 'database', 'intermediate', 3),
                                                                                      (2, 'AWS Lambda', 'cloud', 'intermediate', 3),
                                                                                      (2, 'Docker', 'infrastructure', 'intermediate', 4),
                                                                                      (2, 'REST API Design', 'architecture', 'advanced', 7),
                                                                                      (2, 'CSS/Tailwind', 'frontend', 'expert', 9),
                                                                                      (2, 'Accessibility (WCAG)', 'frontend', 'advanced', 4),
                                                                                      (2, 'Design Systems', 'frontend', 'advanced', 5);

-- Projects
INSERT INTO project (candidate_id, project_name, description, technologies, impact) VALUES
                                                                                        (2, 'Dashboard Rebuild',
                                                                                         'Led migration of customer-facing analytics dashboard from legacy AngularJS to React with TypeScript. Implemented real-time data visualization with WebSocket subscriptions and optimistic UI updates.',
                                                                                         'React, TypeScript, GraphQL, D3.js, WebSocket, Storybook',
                                                                                         'Page load time reduced from 8s to 2s. User engagement increased by 35%. Component library now shared across 3 product teams.'),

                                                                                        (2, 'FHIR Integration Platform',
                                                                                         'Built bidirectional integration layer between the SaaS platform and hospital EHR systems using the FHIR R4 standard. Handled complex data mapping and conflict resolution for patient records.',
                                                                                         'Java 11, Spring Boot 2, React, FHIR R4, PostgreSQL, Docker',
                                                                                         'Enabled 200+ clinics to sync patient data automatically, reducing manual data entry by 90%.');


-- ============================================================
-- Candidate 3: Raj Mehta - Data / ML Engineer
-- ============================================================

INSERT INTO candidate (id, name, email, summary, total_years_exp, location)
VALUES (3, 'Raj Mehta', 'raj.mehta@example.com',
        'Data and ML engineer with 8 years of experience building production ML pipelines, data platforms, and real-time analytics systems. Strong foundation in distributed computing with Spark and Flink. Experienced in deploying and monitoring ML models at scale. Combines software engineering rigor with data science expertise.',
        8, 'Munich, Germany');

-- Work experience
INSERT INTO work_experience (candidate_id, company, role, start_date, end_date, description, technologies) VALUES
                                                                                                               (3, 'InsureAI GmbH', 'Senior ML Engineer', '2022-06-01', NULL,
                                                                                                                'Building and operating ML models for automated insurance claim processing. Designed a feature store serving 50+ models in production. Implemented ML monitoring and drift detection pipelines. Reduced model deployment time from 2 weeks to 2 hours through MLOps automation.',
                                                                                                                'Python, PyTorch, Spark, Airflow, Kubernetes, AWS SageMaker, MLflow, PostgreSQL, Kafka'),

                                                                                                               (3, 'DataScale AG', 'Data Engineer', '2019-01-01', '2022-05-31',
                                                                                                                'Built the company-wide data platform processing 5TB+ daily from 200+ sources. Designed real-time streaming pipelines with Kafka and Flink for fraud detection. Implemented data quality framework with automated anomaly detection.',
                                                                                                                'Python, Scala, Apache Spark, Apache Flink, Kafka, Airflow, AWS Redshift, Delta Lake, Terraform'),

                                                                                                               (3, 'AnalyticsPro', 'Junior Data Engineer', '2017-04-01', '2018-12-31',
                                                                                                                'Built ETL pipelines and reporting dashboards for marketing analytics. Introduced dbt for data transformations, replacing 100+ legacy SQL scripts. Maintained data warehouse serving 50+ internal stakeholders.',
                                                                                                                'Python, SQL, dbt, Apache Airflow, AWS Redshift, Tableau, PostgreSQL');

-- Skills
INSERT INTO skill (candidate_id, skill_name, category, proficiency, years_of_exp) VALUES
                                                                                      (3, 'Python', 'language', 'expert', 8),
                                                                                      (3, 'SQL', 'language', 'expert', 8),
                                                                                      (3, 'Scala', 'language', 'intermediate', 3),
                                                                                      (3, 'Java', 'language', 'intermediate', 2),
                                                                                      (3, 'Apache Spark', 'data', 'expert', 6),
                                                                                      (3, 'Apache Kafka', 'messaging', 'advanced', 5),
                                                                                      (3, 'Apache Flink', 'data', 'advanced', 3),
                                                                                      (3, 'Apache Airflow', 'data', 'expert', 6),
                                                                                      (3, 'PyTorch', 'ml', 'advanced', 4),
                                                                                      (3, 'AWS SageMaker', 'ml', 'advanced', 3),
                                                                                      (3, 'MLflow', 'ml', 'advanced', 3),
                                                                                      (3, 'Kubernetes', 'infrastructure', 'intermediate', 3),
                                                                                      (3, 'PostgreSQL', 'database', 'advanced', 6),
                                                                                      (3, 'AWS Redshift', 'database', 'advanced', 4),
                                                                                      (3, 'Terraform', 'infrastructure', 'intermediate', 3),
                                                                                      (3, 'Docker', 'infrastructure', 'advanced', 5),
                                                                                      (3, 'Data Modeling', 'architecture', 'advanced', 6),
                                                                                      (3, 'MLOps', 'ml', 'advanced', 3);

-- Projects
INSERT INTO project (candidate_id, project_name, description, technologies, impact) VALUES
                                                                                        (3, 'Automated Claims Processing',
                                                                                         'Designed and deployed an ML pipeline that classifies and routes insurance claims automatically. Built a multi-model ensemble combining NLP for document understanding and computer vision for damage assessment from uploaded photos.',
                                                                                         'Python, PyTorch, Spark, AWS SageMaker, Kafka, PostgreSQL',
                                                                                         'Automated 60% of claims processing. Reduced average claim resolution time from 14 days to 3 days. Saved €4M annually in operational costs.'),

                                                                                        (3, 'Real-Time Fraud Detection',
                                                                                         'Built a streaming fraud detection system processing 10K+ transactions per second. Implemented a feature engineering pipeline with Apache Flink and a model serving layer with sub-100ms latency requirements.',
                                                                                         'Scala, Apache Flink, Kafka, Python, Redis, Kubernetes',
                                                                                         'Detected 95% of fraudulent transactions in real-time with a false positive rate under 0.1%. Prevented €12M in fraud losses in the first year.'),

                                                                                        (3, 'Enterprise Data Platform',
                                                                                         'Architected a company-wide data lakehouse on AWS, consolidating data from 200+ sources. Implemented data lineage tracking, automated data quality checks, and self-service analytics layer.',
                                                                                         'Python, Spark, Delta Lake, Airflow, AWS Redshift, dbt, Terraform',
                                                                                         'Reduced data pipeline failures by 80%. Enabled self-service analytics for 50+ non-technical stakeholders. Cut monthly AWS data costs by 40% through optimization.');


-- ============================================================
-- Candidate 4: Sophie Brenner - DevOps / Platform Engineer
-- ============================================================

INSERT INTO candidate (id, name, email, summary, total_years_exp, location)
VALUES (4, 'Sophie Brenner', 'sophie.brenner@example.com',
        'Platform engineer with 10 years of experience building developer platforms, CI/CD systems, and cloud infrastructure. Deep expertise in Kubernetes, AWS, and infrastructure-as-code. Passionate about developer experience, observability, and reducing the gap between development and production. Track record of building internal platforms that serve 100+ engineers.',
        10, 'Frankfurt, Germany');

-- Work experience
INSERT INTO work_experience (candidate_id, company, role, start_date, end_date, description, technologies) VALUES
                                                                                                               (4, 'CloudBank AG', 'Principal Platform Engineer', '2021-09-01', NULL,
                                                                                                                'Leading a team of 6 building the internal developer platform for a digital bank with 150+ engineers. Designed a self-service Kubernetes platform with built-in observability, secrets management, and automated compliance checks. Implemented golden path templates that reduced service bootstrapping from 2 weeks to 30 minutes.',
                                                                                                                'Kubernetes, AWS EKS, Terraform, ArgoCD, Prometheus, Grafana, Go, Backstage, Vault'),

                                                                                                               (4, 'ScaleOps GmbH', 'Senior DevOps Engineer', '2018-01-01', '2021-08-31',
                                                                                                                'Built and maintained CI/CD infrastructure for 80+ microservices. Migrated from on-premise VMs to AWS EKS, achieving 99.99% uptime. Implemented infrastructure-as-code across all environments. Designed disaster recovery procedures and led quarterly chaos engineering exercises.',
                                                                                                                'Kubernetes, AWS, Terraform, Jenkins, Ansible, Docker, Prometheus, ELK Stack, Python'),

                                                                                                               (4, 'NetSolutions AG', 'Systems Administrator', '2015-06-01', '2017-12-31',
                                                                                                                'Managed Linux infrastructure for a hosting provider serving 500+ clients. Automated server provisioning and configuration management. Introduced monitoring and alerting, reducing mean time to detection from hours to minutes.',
                                                                                                                'Linux, Ansible, Nagios, Bash, Python, VMware, HAProxy, PostgreSQL');

-- Skills
INSERT INTO skill (candidate_id, skill_name, category, proficiency, years_of_exp) VALUES
                                                                                      (4, 'Kubernetes', 'infrastructure', 'expert', 7),
                                                                                      (4, 'AWS', 'cloud', 'expert', 8),
                                                                                      (4, 'Terraform', 'infrastructure', 'expert', 6),
                                                                                      (4, 'Docker', 'infrastructure', 'expert', 8),
                                                                                      (4, 'Go', 'language', 'advanced', 4),
                                                                                      (4, 'Python', 'language', 'advanced', 7),
                                                                                      (4, 'Bash', 'language', 'expert', 10),
                                                                                      (4, 'Linux', 'infrastructure', 'expert', 10),
                                                                                      (4, 'ArgoCD', 'devops', 'advanced', 3),
                                                                                      (4, 'Prometheus', 'observability', 'expert', 5),
                                                                                      (4, 'Grafana', 'observability', 'expert', 5),
                                                                                      (4, 'Jenkins', 'devops', 'advanced', 6),
                                                                                      (4, 'Ansible', 'infrastructure', 'advanced', 5),
                                                                                      (4, 'Helm', 'infrastructure', 'advanced', 4),
                                                                                      (4, 'GitOps', 'devops', 'advanced', 4),
                                                                                      (4, 'Vault', 'security', 'intermediate', 3),
                                                                                      (4, 'Backstage', 'devops', 'intermediate', 2);

-- Projects
INSERT INTO project (candidate_id, project_name, description, technologies, impact) VALUES
                                                                                        (4, 'Internal Developer Platform',
                                                                                         'Designed and built a self-service developer platform on top of Kubernetes with integrated CI/CD, observability, and compliance scanning. Developers create new services through a Backstage portal with golden path templates that include pre-configured monitoring, logging, and security baselines.',
                                                                                         'Kubernetes, AWS EKS, ArgoCD, Backstage, Terraform, Go, Prometheus, Grafana',
                                                                                         'Reduced new service bootstrapping from 2 weeks to 30 minutes. Platform adoption reached 95% across 150+ engineers within 6 months.'),

                                                                                        (4, 'Cloud Migration',
                                                                                         'Led migration of 80+ microservices from on-premise VMware infrastructure to AWS EKS. Implemented blue-green deployment strategy with zero-downtime cutovers. Built automated rollback mechanisms and comprehensive health check system.',
                                                                                         'AWS EKS, Terraform, Jenkins, Docker, Ansible, Prometheus, Python',
                                                                                         'Achieved 99.99% uptime post-migration. Reduced infrastructure costs by 35%. Deployment frequency increased from weekly to multiple times daily.'),

                                                                                        (4, 'Chaos Engineering Program',
                                                                                         'Established the company chaos engineering practice. Built custom chaos injection tools targeting Kubernetes pods, network policies, and AWS resources. Designed quarterly game day exercises involving all engineering teams.',
                                                                                         'Go, Kubernetes, Litmus Chaos, Prometheus, Grafana, AWS Fault Injection Simulator',
                                                                                         'Identified and fixed 23 critical resilience gaps before they caused production incidents. Reduced mean time to recovery from 45 minutes to 8 minutes.');
