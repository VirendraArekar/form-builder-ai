package com.formgenius;

import com.formgenius.auth.AuthService;
import com.formgenius.auth.dto.AuthResponse;
import com.formgenius.auth.dto.LoginRequest;
import com.formgenius.auth.dto.RegisterRequest;
import com.formgenius.form.FormService;
import com.formgenius.form.dto.*;
import com.formgenius.publicform.PublicFormService;
import com.formgenius.publicform.dto.PublicFormViewResponse;
import com.formgenius.publicform.dto.PublicSubmissionResultResponse;
import com.formgenius.publicform.dto.SubmitPublicFormRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FormGeniusIntegrationTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private FormService formService;

    @Autowired
    private PublicFormService publicFormService;

    private AuthResponse testUserAuth;
    private UUID workspaceId;

    @BeforeEach
    void setUp() {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(uniqueEmail);
        regReq.setPassword("Password123!");
        regReq.setFirstName("Test");
        regReq.setLastName("User");
        regReq.setCompanyName("Test Inc");

        testUserAuth = authService.register(regReq);
        workspaceId = testUserAuth.getActiveWorkspace().getId();
    }

    @Test
    void testAuthLifecycle() {
        assertNotNull(testUserAuth.getAccessToken());
        assertNotNull(testUserAuth.getRefreshToken());
        assertEquals("Test", testUserAuth.getUser().getFirstName());

        // Login
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail(testUserAuth.getUser().getEmail());
        loginReq.setPassword("Password123!");
        AuthResponse loginRes = authService.login(loginReq);
        assertNotNull(loginRes.getAccessToken());
    }

    @Test
    void testFormCreationAndPublishing() {
        CreateFormRequest createReq = new CreateFormRequest();
        createReq.setWorkspaceId(workspaceId);
        createReq.setTitle("Product Evaluation Survey");
        createReq.setDescription("Customer review of software quality");

        FormDetailResponse form = formService.createForm(testUserAuth.getUser().getId(), createReq);
        assertNotNull(form.getId());
        assertEquals("DRAFT", form.getStatus());

        // Save Form Schema with questions
        SaveFormSchemaRequest schemaReq = new SaveFormSchemaRequest();
        schemaReq.setTitle(form.getTitle());
        schemaReq.setDescription(form.getDescription());

        UUID secId = UUID.randomUUID();
        schemaReq.setSections(List.of(
                FormSectionDto.builder().id(secId).title("General").orderIndex(0).build()
        ));

        UUID q1Id = UUID.randomUUID();
        UUID q2Id = UUID.randomUUID();
        schemaReq.setQuestions(List.of(
                FormQuestionDto.builder().id(q1Id).sectionId(secId).fieldKey("name").title("Full Name").questionType("SHORT_TEXT").isRequired(true).orderIndex(0).build(),
                FormQuestionDto.builder().id(q2Id).sectionId(secId).fieldKey("rating").title("Score").questionType("NUMBER").minValue(1.0).maxValue(10.0).isRequired(true).orderIndex(1).build()
        ));

        FormDetailResponse updated = formService.saveFormSchema(form.getId(), testUserAuth.getUser().getId(), schemaReq);
        assertEquals(2, updated.getQuestions().size());

        // Publish
        PublishFormRequest pubReq = new PublishFormRequest();
        pubReq.setCustomSlug("prod-eval-" + UUID.randomUUID().toString().substring(0, 6));
        FormDetailResponse published = formService.publishForm(form.getId(), testUserAuth.getUser().getId(), pubReq);
        assertEquals("PUBLISHED", published.getStatus());
        assertNotNull(published.getPublishedSlug());

        // Public retrieval
        PublicFormViewResponse publicView = publicFormService.getPublicForm(published.getPublishedSlug(), null, null);
        assertNotNull(publicView);
        assertEquals("Product Evaluation Survey", publicView.getTitle());
        assertEquals(2, publicView.getQuestions().size());

        // Public submission with valid answers
        SubmitPublicFormRequest subReq = new SubmitPublicFormRequest();
        subReq.setDurationSeconds(45);
        Map<String, Object> answers = new HashMap<>();
        answers.put("name", "Alice Wonder");
        answers.put("rating", 9);
        subReq.setAnswers(answers);

        PublicSubmissionResultResponse subRes = publicFormService.submitPublicForm(published.getPublishedSlug(), subReq, null);
        assertNotNull(subRes.getSubmissionId());
    }

    @Test
    void testFormDuplicate() {
        CreateFormRequest createReq = new CreateFormRequest();
        createReq.setWorkspaceId(workspaceId);
        createReq.setTitle("Original Form");
        FormDetailResponse form = formService.createForm(testUserAuth.getUser().getId(), createReq);

        FormDetailResponse duplicate = formService.duplicateForm(form.getId(), testUserAuth.getUser().getId());
        assertNotNull(duplicate.getId());
        assertNotEquals(form.getId(), duplicate.getId());
        assertTrue(duplicate.getTitle().contains("Copy"));
    }
}
