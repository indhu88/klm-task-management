/**
 * This package contains security-related classes and components
 * for the Task Management application’s user authentication and authorization.
 *
 * It includes implementations of Spring Security interfaces,
 * JWT token utilities, and filters to secure user access and manage authentication.
 */
package com.klm.taskmanagement.security;

import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of Spring Security's {@link UserDetails} interface
 * that adapts the application {@link User} entity to the security framework.
 *
 * This class provides user authentication details required by Spring Security.
 */
@Data
public class UserInfoDetails implements UserDetails {

    private final User user;
    public UserInfoDetails(User user) {
        this.user = user;
    }
    /**
     * Returns the password used to authenticate the user.
     *
     * @return the user's hashed password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    /**
     * Returns the authorities granted to the user.
     *
     * Note: Currently returns an empty list; roles should be mapped to authorities if needed.
     *
     * @return the authorities granted to the user.
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
    /**
     * Returns the username used to authenticate the user.
     *
     * @return the user's username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is non-expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the account is non-locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the credentials are non-expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
    /**
     * Returns the roles assigned to the user.
     *
     * @return a set of roles associated with the user.
     */
    public Set<Role> getRoles() {
        return user.getRoles();
    }
}
