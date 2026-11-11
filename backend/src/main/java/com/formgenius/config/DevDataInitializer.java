package com.formgenius.config;

import com.formgenius.form.*;
import com.formgenius.user.*;
import com.formgenius.workspace.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
public class DevDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevDataInitializer.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final FormRepository formRepository;
    private final FormQuestionRepository formQuestionRepository;
    private final FormQuestionOptionRepository formQuestionOptionRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataInitializer(UserRepository userRepository,
                              UserProfileRepository userProfileRepository,
                              WorkspaceRepository workspaceRepository,
                              WorkspaceMemberRepository workspaceMemberRepository,
                              FormRepository formRepository,
                              FormQuestionRepository formQuestionRepository,
                              FormQuestionOptionRepository formQuestionOptionRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.formRepository = formRepository;
        this.formQuestionRepository = formQuestionRepository;
        this.formQuestionOptionRepository = formQuestionOptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByEmail("alex@formgenius.ai").isPresent()) {
            log.info("Dev seed data already present.");
            return;
        }

        log.info("Initializing in-memory development seed data...");

        User user = new User();
        user.setEmail("alex@formgenius.ai");
        user.setPasswordHash(passwordEncoder.encode("Password123!"));
        user.setFirstName("Alex");
        user.setLastName("Morgan");
        user.setSystemRole("SUPER_ADMIN");
        user.setEmailVerified(true);
        user.setActive(true);
        user = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setJobTitle("Lead Product Architect");
        profile.setCompanyName("Acme Form Systems");
        profile.setPreferredTheme("light");
        profile.setTimezone("UTC");
        userProfileRepository.save(profile);

        Workspace workspace = new Workspace();
        workspace.setName("Acme Inc");
        workspace.setSlug("acme-workspace");
        workspace.setDescription("Main engineering and product workspace for FormGenius testing.");
        workspace.setOwner(user);
        workspace.setPlanTier("BUSINESS");
        workspace.setMaxForms(100);
        workspace.setMaxResponsesPerMonth(10000);
        workspace.setAiGenerationsQuota(1000);
        workspace = workspaceRepository.save(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setRole("WORKSPACE_OWNER");
        workspaceMemberRepository.save(member);

        Form form = new Form();
        form.setWorkspace(workspace);
        form.setCreatedBy(user);
        form.setTitle("Customer Satisfaction & Product Feedback Survey");
        form.setDescription("We value your continuous feedback to help elevate our product experience.");
        form.setStatus("PUBLISHED");
        form.setPublishedSlug("customer-feedback-demo");
        form.setAllowMultipleSubmissions(true);
        form = formRepository.save(form);

        FormQuestion q1 = new FormQuestion();
        q1.setForm(form);
        q1.setFieldKey("full_name");
        q1.setTitle("Full Name");
        q1.setQuestionType("SHORT_TEXT");
        q1.setRequired(true);
        q1.setOrderIndex(0);
        q1.setPlaceholder("e.g. Jane Doe");
        formQuestionRepository.save(q1);

        FormQuestion q2 = new FormQuestion();
        q2.setForm(form);
        q2.setFieldKey("email");
        q2.setTitle("Email Address");
        q2.setQuestionType("EMAIL");
        q2.setRequired(true);
        q2.setOrderIndex(1);
        q2.setPlaceholder("jane@example.com");
        formQuestionRepository.save(q2);

        FormQuestion q3 = new FormQuestion();
        q3.setForm(form);
        q3.setFieldKey("satisfaction");
        q3.setTitle("How satisfied are you with our platform?");
        q3.setQuestionType("RATING");
        q3.setRequired(true);
        q3.setOrderIndex(2);
        formQuestionRepository.save(q3);

        FormQuestion q4 = new FormQuestion();
        q4.setForm(form);
        q4.setFieldKey("primary_use_case");
        q4.setTitle("What is your primary use case?");
        q4.setQuestionType("RADIO");
        q4.setRequired(true);
        q4.setOrderIndex(3);
        q4 = formQuestionRepository.save(q4);

        FormQuestionOption opt1 = new FormQuestionOption();
        opt1.setQuestion(q4);
        opt1.setLabel("Customer Feedback");
        opt1.setValue("Customer Feedback");
        opt1.setOrderIndex(0);
        formQuestionOptionRepository.save(opt1);

        FormQuestionOption opt2 = new FormQuestionOption();
        opt2.setQuestion(q4);
        opt2.setLabel("Lead Generation");
        opt2.setValue("Lead Generation");
        opt2.setOrderIndex(1);
        formQuestionOptionRepository.save(opt2);

        FormQuestionOption opt3 = new FormQuestionOption();
        opt3.setQuestion(q4);
        opt3.setLabel("Product Research");
        opt3.setValue("Product Research");
        opt3.setOrderIndex(2);
        formQuestionOptionRepository.save(opt3);

        FormQuestion q5 = new FormQuestion();
        q5.setForm(form);
        q5.setFieldKey("comments");
        q5.setTitle("Any additional comments or suggestions?");
        q5.setQuestionType("LONG_TEXT");
        q5.setRequired(false);
        q5.setOrderIndex(4);
        formQuestionRepository.save(q5);

        log.info("Dev seed data initialized successfully! Default login: alex@formgenius.ai / Password123!");
    }
}
