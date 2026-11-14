package com.formgenius.analytics;

import com.formgenius.workspace.Workspace;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "workspace_analytics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workspace_id", "date"})
})
public class WorkspaceAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_views", nullable = false)
    private int totalViews = 0;

    @Column(name = "total_submissions", nullable = false)
    private int totalSubmissions = 0;

    @Column(name = "active_forms", nullable = false)
    private int activeForms = 0;

    public WorkspaceAnalytics() {}

    public WorkspaceAnalytics(UUID id, Workspace workspace, LocalDate date, int totalViews, int totalSubmissions, int activeForms) {
        this.id = id;
        this.workspace = workspace;
        this.date = date;
        this.totalViews = totalViews;
        this.totalSubmissions = totalSubmissions;
        this.activeForms = activeForms;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public int getTotalSubmissions() {
        return totalSubmissions;
    }

    public int getActiveForms() {
        return activeForms;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }

    public void setActiveForms(int activeForms) {
        this.activeForms = activeForms;
    }

    public static WorkspaceAnalyticsBuilder builder() {
        return new WorkspaceAnalyticsBuilder();
    }
    public static class WorkspaceAnalyticsBuilder {
        private UUID id;
        private Workspace workspace;
        private LocalDate date;
        private int totalViews = 0;
        private int totalSubmissions = 0;
        private int activeForms = 0;

        public WorkspaceAnalyticsBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public WorkspaceAnalyticsBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }
        public WorkspaceAnalyticsBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public WorkspaceAnalyticsBuilder totalViews(int totalViews) {
            this.totalViews = totalViews;
            return this;
        }
        public WorkspaceAnalyticsBuilder totalSubmissions(int totalSubmissions) {
            this.totalSubmissions = totalSubmissions;
            return this;
        }
        public WorkspaceAnalyticsBuilder activeForms(int activeForms) {
            this.activeForms = activeForms;
            return this;
        }

        public WorkspaceAnalytics build() {
            WorkspaceAnalytics instance = new WorkspaceAnalytics();
            instance.id = this.id;
            instance.workspace = this.workspace;
            instance.date = this.date;
            instance.totalViews = this.totalViews;
            instance.totalSubmissions = this.totalSubmissions;
            instance.activeForms = this.activeForms;
            return instance;
        }
    }
}
