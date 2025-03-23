package com.ortim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ortim.core.utils.DbConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = DbConstants.roleTableName)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseModel {


    @Column(name = DbConstants.roleTableColumnName, unique = true, length = DbConstants.textTallSize)
    private String name;

    @Column(name = DbConstants.roleTableColumnDescription, length = DbConstants.textGrandeSize)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = DbConstants.roleTableName)
    private Set<User> users;

}
