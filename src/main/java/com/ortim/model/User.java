package com.ortim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ortim.core.utils.DbConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = DbConstants.userTableName)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseModel implements UserDetails {

    private static final long serialVersion = 1L;

    @Column(name = DbConstants.userEmail, unique = true,
            nullable = false)
    @Size(max = DbConstants.textVentiSize)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = DbConstants.userName, length = DbConstants.textTallSize)
    private String name;

    @Column(name = DbConstants.userLastName, length = DbConstants.textGrandeSize)
    private String lastName;

    @NotBlank
    @JsonIgnore
    @Column(name = DbConstants.userPassword)
    @Size(min = DbConstants.minPasswordSize, max = DbConstants.textVentiSize)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSocialAccount> socialAccounts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(
            name = DbConstants.userRoleTableName,
            joinColumns = @JoinColumn(name = DbConstants.userRoleTableNameUserId),
            inverseJoinColumns = @JoinColumn(name = DbConstants.userRoleTableNameRoleId)
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = DbConstants.userIsActive, nullable = false)
    private Boolean isActive;

    @Column(name = DbConstants.userIsDeleted, nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<FileEntity> profilePhotos = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(e -> new SimpleGrantedAuthority("ROLE_" + e.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
        return Boolean.TRUE.equals(isActive) && Boolean.FALSE.equals(isDeleted);
    }

}

