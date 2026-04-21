package com.quickapp.auth.dto;

import java.util.List;

public class UserVM {
    private String id;
    private String userName;
    private String fullName;
    private String email;
    private String jobTitle;
    private String phoneNumber;
    private boolean isEnabled;
    private boolean isLockedOut;
    private List<String> roles;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(boolean isEnabled) { this.isEnabled = isEnabled; }
    public boolean getIsLockedOut() { return isLockedOut; }
    public void setIsLockedOut(boolean isLockedOut) { this.isLockedOut = isLockedOut; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
