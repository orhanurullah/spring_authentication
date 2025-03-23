package com.ortim.model;

import com.ortim.core.utils.DbConstants;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = DbConstants.socialAccountTableName)
public class UserSocialAccount extends BaseModel {

    @Enumerated(EnumType.STRING)
    @Column(name = DbConstants.socialAccountTableProvider, nullable = false)
    private AuthProvider provider; // OAuth2 sağlayıcısı (GOOGLE, GITHUB, FACEBOOK vb.)

    @Column(name = DbConstants.socialAccountTableProviderId, nullable = false, unique = true)
    private String providerId; // Sağlayıcı tarafından verilen benzersiz kullanıcı kimliği

    @Column(name = DbConstants.socialAccountTableProfileImageThumbnail)
    private String profileImageUrl; // Profil fotoğrafı URL'si (varsa)

    @Column(name = DbConstants.socialAccountTableFullName)
    private String fullName; // Sağlayıcıdan gelen tam isim (opsiyonel)

    @ManyToOne
    @JoinColumn(name = DbConstants.socialAccountTableUserId, nullable = false)
    private User user;

}

