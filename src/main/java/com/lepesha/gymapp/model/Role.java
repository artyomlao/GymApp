package com.lepesha.gymapp.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(new HashSet<>(Arrays.
            asList(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_WRITE))),
    USER(new HashSet<>(Arrays.
            asList(Permission.DEVELOPERS_READ)));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions){
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        return getPermissions().stream().
                map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
