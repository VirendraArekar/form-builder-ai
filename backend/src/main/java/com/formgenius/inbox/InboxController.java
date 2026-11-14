package com.formgenius.inbox;

import com.formgenius.common.ApiResponse;
import com.formgenius.common.PageResponse;
import com.formgenius.inbox.dto.InboxItemDto;
import com.formgenius.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping({"/api/inbox", "/api/v1/inbox"})
@Tag(name = "Unified Inbox", description = "Cross-form submissions inbox for rapid triage, review, and answer inspection")
public class InboxController {

    private final InboxService inboxService;

    @GetMapping
    @Operation(summary = "Get unified submissions across all user forms")
    public ResponseEntity<ApiResponse<PageResponse<InboxItemDto>>> getInbox(
            @RequestParam(required = false) UUID formId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "submittedAt,desc") String sort,
            @AuthenticationPrincipal UserPrincipal principal) {

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<InboxItemDto> result = inboxService.getInboxSubmissions(principal.getId(), formId, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(result)));
    }

    public InboxController(InboxService inboxService) {
        this.inboxService = inboxService;
    }
}
