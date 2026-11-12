package com.formgenius.form;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "form_themes")
public class FormTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false, unique = true)
    private Form form;

    @Column(name = "primary_color")
    private String primaryColor = "#4F46E5";

    @Column(name = "background_color")
    private String backgroundColor = "#FFFFFF";

    @Column(name = "text_color")
    private String textColor = "#111827";

    @Column(name = "font_family")
    private String fontFamily = "Inter";

    @Column(name = "border_radius")
    private String borderRadius = "md";

    @Column(name = "header_image_url")
    private String headerImageUrl;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "button_style")
    private String buttonStyle = "rounded";

    @Column(name = "layout_mode")
    private String layoutMode = "single_page"; // single_page, card_per_step

    @Column(name = "show_progress_bar", nullable = false)
    private boolean showProgressBar = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public FormTheme() {}

    public FormTheme(UUID id, Form form, String primaryColor, String backgroundColor, String textColor, String fontFamily, String borderRadius, String headerImageUrl, String logoUrl, String buttonStyle, String layoutMode, boolean showProgressBar, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.form = form;
        this.primaryColor = primaryColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.fontFamily = fontFamily;
        this.borderRadius = borderRadius;
        this.headerImageUrl = headerImageUrl;
        this.logoUrl = logoUrl;
        this.buttonStyle = buttonStyle;
        this.layoutMode = layoutMode;
        this.showProgressBar = showProgressBar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public Form getForm() {
        return form;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public String getBorderRadius() {
        return borderRadius;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public String getLayoutMode() {
        return layoutMode;
    }

    public boolean isShowProgressBar() {
        return showProgressBar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public void setBorderRadius(String borderRadius) {
        this.borderRadius = borderRadius;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public void setLayoutMode(String layoutMode) {
        this.layoutMode = layoutMode;
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static FormThemeBuilder builder() {
        return new FormThemeBuilder();
    }
    public static class FormThemeBuilder {
        private UUID id;
        private Form form;
        private String primaryColor = "#4F46E5";
        private String backgroundColor = "#FFFFFF";
        private String textColor = "#111827";
        private String fontFamily = "Inter";
        private String borderRadius = "md";
        private String headerImageUrl;
        private String logoUrl;
        private String buttonStyle = "rounded";
        private String layoutMode = "single_page";
        private boolean showProgressBar = true;
        private Instant createdAt;
        private Instant updatedAt;

        public FormThemeBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        public FormThemeBuilder form(Form form) {
            this.form = form;
            return this;
        }
        public FormThemeBuilder primaryColor(String primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }
        public FormThemeBuilder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
        public FormThemeBuilder textColor(String textColor) {
            this.textColor = textColor;
            return this;
        }
        public FormThemeBuilder fontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }
        public FormThemeBuilder borderRadius(String borderRadius) {
            this.borderRadius = borderRadius;
            return this;
        }
        public FormThemeBuilder headerImageUrl(String headerImageUrl) {
            this.headerImageUrl = headerImageUrl;
            return this;
        }
        public FormThemeBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }
        public FormThemeBuilder buttonStyle(String buttonStyle) {
            this.buttonStyle = buttonStyle;
            return this;
        }
        public FormThemeBuilder layoutMode(String layoutMode) {
            this.layoutMode = layoutMode;
            return this;
        }
        public FormThemeBuilder showProgressBar(boolean showProgressBar) {
            this.showProgressBar = showProgressBar;
            return this;
        }
        public FormThemeBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public FormThemeBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FormTheme build() {
            FormTheme instance = new FormTheme();
            instance.id = this.id;
            instance.form = this.form;
            instance.primaryColor = this.primaryColor;
            instance.backgroundColor = this.backgroundColor;
            instance.textColor = this.textColor;
            instance.fontFamily = this.fontFamily;
            instance.borderRadius = this.borderRadius;
            instance.headerImageUrl = this.headerImageUrl;
            instance.logoUrl = this.logoUrl;
            instance.buttonStyle = this.buttonStyle;
            instance.layoutMode = this.layoutMode;
            instance.showProgressBar = this.showProgressBar;
            instance.createdAt = this.createdAt;
            instance.updatedAt = this.updatedAt;
            return instance;
        }
    }
}
