/**
 * This package contains entity classes representing the core domain
 * objects of the user management module in the task management system.
 */
package com.klm.taskmanagement.user.entity;
/**
 * Enum representing user roles within the system.
 *
 * Roles define the level of access and permissions granted to a user.
 *
 * Available roles:
 * <ul>
 *   <li>USER - Standard user with limited privileges.</li>
 *   <li>ADMIN - Administrator with full access rights.</li>
 * </ul>
 */
public enum Role {
    ROLE_USER,ROLE_ADMIN
}
