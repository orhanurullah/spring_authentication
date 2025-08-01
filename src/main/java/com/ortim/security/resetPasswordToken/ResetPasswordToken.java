package com.ortim.security.resetPasswordToken;

import com.ortim.core.utils.DbConstants;
import com.ortim.core.utils.TimeUtil;
import com.ortim.model.BaseModel;
import com.ortim.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DbConstants.resetPasswordTokenTableName)
public class ResetPasswordToken extends BaseModel {

    @Size(max = 255)
    private String token;

    @OneToOne(targetEntity = User.class,
            fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = DbConstants.resetPasswordTokenUserColumnName)
    private User user;

    @Column(name = DbConstants.resetPasswordTokenExpiryDateColumnName, nullable = false)
    private LocalDateTime expiryDate;

    public ResetPasswordToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
    public void onCreate() {
        setExpiryDate(TimeUtil.now().plusMinutes(30));
    }
}
