package com.quickapp.auth.model;

public class Permission {
    private String name;
    private String value;
    private String groupName;
    private String description;

    public Permission() {}

    public Permission(String name, String value, String groupName, String description) {
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

    public static final Permission VIEW_USERS = new Permission(
            "View Users", "users.view", "User Permissions",
            "Permission to view other users account details");
    public static final Permission MANAGE_USERS = new Permission(
            "Manage Users", "users.manage", "User Permissions",
            "Permission to create, delete and modify other users account details");
    public static final Permission VIEW_ROLES = new Permission(
            "View Roles", "roles.view", "Role Permissions",
            "Permission to view available roles");
    public static final Permission MANAGE_ROLES = new Permission(
            "Manage Roles", "roles.manage", "Role Permissions",
            "Permission to create, delete and modify roles");
    public static final Permission ASSIGN_ROLES = new Permission(
            "Assign Roles", "roles.assign", "Role Permissions",
            "Permission to assign roles to users");

    public static final Permission[] ALL_PERMISSIONS = {
            VIEW_USERS, MANAGE_USERS, VIEW_ROLES, MANAGE_ROLES, ASSIGN_ROLES
    };

    public static String[] getAllPermissionValues() {
        String[] values = new String[ALL_PERMISSIONS.length];
        for (int i = 0; i < ALL_PERMISSIONS.length; i++) {
            values[i] = ALL_PERMISSIONS[i].getValue();
        }
        return values;
    }
}
