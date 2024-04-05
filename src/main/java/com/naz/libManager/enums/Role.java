package com.naz.libManager.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PATRON,
    ADMIN;

    /**
     * @return
     */
    @Override
    public String getAuthority() {
        return name();
    }
}
