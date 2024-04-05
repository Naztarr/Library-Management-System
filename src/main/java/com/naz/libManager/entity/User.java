package com.naz.libManager.entity;

import com.naz.libManager.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String emailAddress;

    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    private List<Book> borrowedBooks = new ArrayList<>();

    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
