package com.community.jboss.leadmanagement.data.models;

public class ContributorData {
    private String login;
    private String avatarUrl;
    private String githubProfileUrl;
    private int contributions;

    public ContributorData(String login, String avatarUrl, String gitHubProfileUrl, int contributions) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.githubProfileUrl = gitHubProfileUrl;
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getGithubProfileUrl() {
        return githubProfileUrl;
    }

    public int getContributions() {
        return contributions;
    }
}