package com.config;

public class Configuration {

    private String pptpDestination;
    private String pptpTemplatePath;
    private String datasourceUrl;
    private String datasourceUN;
    private String datasourcePW;
    private String qrCodeUrl;
    private String token;
    private String downloadService;
    private String checkMarkImagePath;

    public String getPptpDestination() {
        return pptpDestination;
    }

    public void setPptpDestination(String pptpDestination) {
        this.pptpDestination = pptpDestination;
    }

    public String getPptpTemplatePath() {
        return pptpTemplatePath;
    }

    public void setPptpTemplatePath(String pptpTemplatePath) {
        this.pptpTemplatePath = pptpTemplatePath;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public String getDatasourceUN() {
        return datasourceUN;
    }

    public void setDatasourceUN(String datasourceUN) {
        this.datasourceUN = datasourceUN;
    }

    public String getDatasourcePW() {
        return datasourcePW;
    }

    public void setDatasourcePW(String datasourcePW) {
        this.datasourcePW = datasourcePW;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(String downloadService) {
        this.downloadService = downloadService;
    }

    public String getCheckMarkImagePath() {
        return checkMarkImagePath;
    }

    public void setCheckMarkImagePath(String checkMarkImagePath) {
        this.checkMarkImagePath = checkMarkImagePath;
    }
}
