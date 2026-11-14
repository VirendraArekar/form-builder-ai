package com.formgenius.response.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public class BulkDeleteRequest {
    @NotEmpty(message = "Response IDs cannot be empty")
    private List<UUID> responseIds;

    public BulkDeleteRequest() {}

    public BulkDeleteRequest(List<UUID> responseIds) {
        this.responseIds = responseIds;
    }

    public List<UUID> getResponseIds() {
        return responseIds;
    }

    public void setResponseIds(List<UUID> responseIds) {
        this.responseIds = responseIds;
    }
}
