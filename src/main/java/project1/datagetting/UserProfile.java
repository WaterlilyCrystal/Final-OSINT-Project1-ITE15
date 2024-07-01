package project1.datagetting;

import java.util.StringJoiner;

public class UserProfile {
    private String name;
    private String userId;
    private String socialAccount;
    private int gender;
    private int score;
    private int comments;
    private int followers;
    private int followings;
    private int createdPosts;


    public UserProfile(String name, String userId, String socialAccount, int gender, int score, int comments, int followers, int followings, int createdPosts) {
        this.name = name;
        this.userId = userId;
        this.socialAccount = socialAccount;
        this.gender = gender;
        this.score = score;
        this.comments = comments;
        this.followers = followers;
        this.followings = followings;
        this.createdPosts = createdPosts;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getUserId() {
        return userId;
    }

    public int getGender() {
        return gender;
    }

    public int getComments() {
        return comments;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowings() {
        return followings;
    }

    public int getCreatedPosts() {
        return createdPosts;
    }


    public String getSocialAccount() {
        return socialAccount;
    }
    // Method to convert UserProfile to CSV string

    public String toCSV() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(escapeString(name));
        joiner.add(escapeString(userId));
        joiner.add(escapeString(socialAccount));
        joiner.add(Integer.toString(gender));
        joiner.add(Integer.toString(score));
        joiner.add(Integer.toString(comments));
        joiner.add(Integer.toString(followers));
        joiner.add(Integer.toString(followings));
        joiner.add(Integer.toString(createdPosts));
        return joiner.toString();
    }

    // Method to escape special characters in a CSV field
    private String escapeString(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}


