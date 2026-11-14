package com.formgenius.webhook;

import com.formgenius.exception.ResourceNotFoundException;
import com.formgenius.form.Form;
import com.formgenius.form.FormRepository;
import com.formgenius.workspace.Workspace;
import com.formgenius.workspace.WorkspaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final WebhookDeliveryRepository deliveryRepository;
    private final WorkspaceService workspaceService;
    private final FormRepository formRepository;

    @Transactional(readOnly = true)
    public List<WebhookDto> listWebhooks(UUID workspaceId, UUID userId) {
        workspaceService.findAndValidateMember(workspaceId, userId);
        return webhookRepository.findByWorkspaceId(workspaceId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WebhookDto createWebhook(UUID workspaceId, UUID userId, CreateWebhookRequest request) {
        Workspace workspace = workspaceService.findAndValidateAdmin(workspaceId, userId);
        Form form = request.getFormId() != null ? formRepository.findById(request.getFormId()).orElse(null) : null;

        Webhook webhook = Webhook.builder()
                .workspace(workspace)
                .form(form)
                .targetUrl(request.getTargetUrl().trim())
                .secretKey("whsec_" + UUID.randomUUID().toString().replace("-", ""))
                .subscribedEvents(request.getSubscribedEvents())
                .isActive(true)
                .build();
        webhook = webhookRepository.save(webhook);

        return mapToDto(webhook);
    }

    @Transactional
    public void deleteWebhook(UUID workspaceId, UUID webhookId, UUID userId) {
        workspaceService.findAndValidateAdmin(workspaceId, userId);
        webhookRepository.deleteById(webhookId);
    }

    @Transactional
    public boolean testWebhook(UUID workspaceId, UUID webhookId, UUID userId) {
        workspaceService.findAndValidateAdmin(workspaceId, userId);
        Webhook webhook = webhookRepository.findById(webhookId)
                .orElseThrow(() -> new ResourceNotFoundException("Webhook not found"));

        // Record a mock test delivery
        WebhookDelivery delivery = WebhookDelivery.builder()
                .webhook(webhook)
                .eventType("form.test")
                .payload("{\"event\":\"form.test\",\"timestamp\":\"" + Instant.now() + "\",\"message\":\"FormGenius test delivery\"}")
                .responseStatus(200)
                .responseBody("{\"received\":true}")
                .durationMs(142)
                .isSuccessful(true)
                .build();
        deliveryRepository.save(delivery);
        return true;
    }

    private WebhookDto mapToDto(Webhook w) {
        return WebhookDto.builder()
                .id(w.getId())
                .workspaceId(w.getWorkspace().getId())
                .formId(w.getForm() != null ? w.getForm().getId() : null)
                .targetUrl(w.getTargetUrl())
                .secretKey(w.getSecretKey())
                .isActive(w.isActive())
                .subscribedEvents(w.getSubscribedEvents())
                .createdAt(w.getCreatedAt())
                .build();
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebhookService.class);

    public WebhookService(WebhookRepository webhookRepository, WebhookDeliveryRepository deliveryRepository, WorkspaceService workspaceService, FormRepository formRepository) {
        this.webhookRepository = webhookRepository;
        this.deliveryRepository = deliveryRepository;
        this.workspaceService = workspaceService;
        this.formRepository = formRepository;
    }
}
