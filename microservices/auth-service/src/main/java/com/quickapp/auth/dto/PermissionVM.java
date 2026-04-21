package com.quickapp.auth.dto;

public class PermissionVM {
    private String name;
    private String value;
    private String groupName;
    private String description;

    public PermissionVM() {}

    public PermissionVM(String name, String value, String groupName, String description) {
        this.name = name;
        this.value = value;
        this.groupName = groupName;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
