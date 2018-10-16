package com.jiangxufa.businesslibrary.model;

@SuppressWarnings("unused")
public class AppConfig {

    /***
     * "aboutUsUrl":"https://dev.ifeixiu.com",
     * "agreementUrl":"https://dev.ifeixiu.com",
     * "customerServiceUrl":"https://dev.ifeixiu.com/mobile/#/cs",
     * "homePageUrl":"https://dev.ifeixiu.com",
     * "publicResourceUrl":"https://oss.ifeixiu.com",
     * "routeUrl":"https://dev.ifeixiu.com/router/",
     * "startPicUrl":""
     */

    private String homePageUrl;
    private String appLogoUrl;
    private String aboutUsUrl;
    private String startPicUrl;
    private String agreementUrl;
    private String routeUrl;
    private String customerServiceUrl;
    private String publicResourceUrl;
//    private List<Version> versionList;
//    private List<ExpertPhones> expertPhones;

    public String getCustomerServiceUrl() {
        return customerServiceUrl;
    }

    public void setCustomerServiceUrl(String customerServiceUrl) {
        this.customerServiceUrl = customerServiceUrl;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }

   
    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

   
    public String getAppLogoUrl() {
        return appLogoUrl;
    }

    public void setAppLogoUrl(String appLogoUrl) {
        this.appLogoUrl = appLogoUrl;
    }

   
    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        this.aboutUsUrl = aboutUsUrl;
    }

   
    public String getStartPicUrl() {
        return startPicUrl;
    }

    public void setStartPicUrl(String startPicUrl) {
        this.startPicUrl = startPicUrl;
    }

    public String getPublicResourceUrl() {
        return publicResourceUrl;
    }

    public void setPublicResourceUrl(String publicResourceUrl) {
        this.publicResourceUrl = publicResourceUrl;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "homePageUrl='" + homePageUrl + '\'' +
                ", appLogoUrl='" + appLogoUrl + '\'' +
                ", aboutUsUrl='" + aboutUsUrl + '\'' +
                ", startPicUrl='" + startPicUrl + '\'' +
                ", agreementUrl='" + agreementUrl + '\'' +
                ", routeUrl='" + routeUrl + '\'' +
                ", customerServiceUrl='" + customerServiceUrl + '\'' +
                ", publicResourceUrl='" + publicResourceUrl + '\'' +
                '}';
    }

    //    @NonNull
//    public List<Version> getVersionList() {
//        return versionList == null ? new ArrayList<Version>() : versionList;
//    }
//
//    public void setVersionList(List<Version> versionList) {
//        this.versionList = versionList;
//    }
//
//    public List<ExpertPhones> getExpertPhones() {
//        return expertPhones;
//    }
//
//    public void setExpertPhones(List<ExpertPhones> expertPhones) {
//        this.expertPhones = expertPhones;
//    }
}
