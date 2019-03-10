package com.niaoren.eurekaclientsign.entity;

public class ProtocolservicePrice {
    private Integer id;

    private Integer onlineVideoConference;

    private Integer videoPromotion;

    private Integer dailyVisit;

    private Integer questionSurvey;

    private Integer archives;

    private Integer advisoryReport;

    private Integer offlineDepartments;

    private Integer agreementId;

    public ProtocolservicePrice(Integer id, Integer onlineVideoConference, Integer videoPromotion, Integer dailyVisit, Integer questionSurvey, Integer archives, Integer advisoryReport, Integer offlineDepartments, Integer agreementId) {
        this.id = id;
        this.onlineVideoConference = onlineVideoConference;
        this.videoPromotion = videoPromotion;
        this.dailyVisit = dailyVisit;
        this.questionSurvey = questionSurvey;
        this.archives = archives;
        this.advisoryReport = advisoryReport;
        this.offlineDepartments = offlineDepartments;
        this.agreementId = agreementId;
    }

    public ProtocolservicePrice() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOnlineVideoConference() {
        return onlineVideoConference;
    }

    public void setOnlineVideoConference(Integer onlineVideoConference) {
        this.onlineVideoConference = onlineVideoConference;
    }

    public Integer getVideoPromotion() {
        return videoPromotion;
    }

    public void setVideoPromotion(Integer videoPromotion) {
        this.videoPromotion = videoPromotion;
    }

    public Integer getDailyVisit() {
        return dailyVisit;
    }

    public void setDailyVisit(Integer dailyVisit) {
        this.dailyVisit = dailyVisit;
    }

    public Integer getQuestionSurvey() {
        return questionSurvey;
    }

    public void setQuestionSurvey(Integer questionSurvey) {
        this.questionSurvey = questionSurvey;
    }

    public Integer getArchives() {
        return archives;
    }

    public void setArchives(Integer archives) {
        this.archives = archives;
    }

    public Integer getAdvisoryReport() {
        return advisoryReport;
    }

    public void setAdvisoryReport(Integer advisoryReport) {
        this.advisoryReport = advisoryReport;
    }

    public Integer getOfflineDepartments() {
        return offlineDepartments;
    }

    public void setOfflineDepartments(Integer offlineDepartments) {
        this.offlineDepartments = offlineDepartments;
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }
}