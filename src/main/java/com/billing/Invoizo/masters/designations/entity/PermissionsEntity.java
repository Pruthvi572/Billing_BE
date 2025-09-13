package com.billing.Invoizo.masters.designations.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "setup_permissions")
public class PermissionsEntity {
    @Id
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "serialno", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int serialNo;

    @Column(name = "type", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int type;

    @JsonProperty
    @Column(name = "isenabled", nullable = false, columnDefinition = "boolean default false")
    private boolean isEnabled;

    @Column(name = "module", length = 255, nullable = true)
    private String module;

    @Column(name = "title", length = 255, nullable = true)
    private String title;

    @Column(name = "modulename", length = 255, nullable = true)
    private String moduleName;

    @Transient
    @JsonProperty
    private boolean isAssigned;

    @Column(name = "permissionorder", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int permissionOrder;

}
