package com.formgenius.form.dto;


public class FormThemeDto {
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

    public FormThemeDto() {}

    public FormThemeDto(String primaryColor, String backgroundColor, String textColor, String fontFamily, String borderRadius, String headerImageUrl, String logoUrl, String buttonStyle, String layoutMode, boolean showProgressBar) {
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

    public static FormThemeDtoBuilder builder() {
        return new FormThemeDtoBuilder();
    }
    public static class FormThemeDtoBuilder {
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

        public FormThemeDtoBuilder primaryColor(String primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }
        public FormThemeDtoBuilder backgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }
        public FormThemeDtoBuilder textColor(String textColor) {
            this.textColor = textColor;
            return this;
        }
        public FormThemeDtoBuilder fontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }
        public FormThemeDtoBuilder borderRadius(String borderRadius) {
            this.borderRadius = borderRadius;
            return this;
        }
        public FormThemeDtoBuilder headerImageUrl(String headerImageUrl) {
            this.headerImageUrl = headerImageUrl;
            return this;
        }
        public FormThemeDtoBuilder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }
        public FormThemeDtoBuilder buttonStyle(String buttonStyle) {
            this.buttonStyle = buttonStyle;
            return this;
        }
        public FormThemeDtoBuilder layoutMode(String layoutMode) {
            this.layoutMode = layoutMode;
            return this;
        }
        public FormThemeDtoBuilder showProgressBar(boolean showProgressBar) {
            this.showProgressBar = showProgressBar;
            return this;
        }

        public FormThemeDto build() {
            FormThemeDto instance = new FormThemeDto();
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
            return instance;
        }
    }
}
