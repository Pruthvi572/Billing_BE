package com.billing.Invoizo.masters.designation_permission.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Entity
@Table(name = "master_designation_permissions")
@EqualsAndHashCode
@IdClass(CompositeDesignationPermissionId.class)
public class DesignationPermissionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column()
    private String permissions;

    @Id
    @Column(name = "designationid", nullable = false, columnDefinition = "int default 0")
    private int designationId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designationid", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private DesignationsEntity designationsEntity;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int type;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String module;

    @Column(name = "modulename", length = 255)
    private String moduleName;

    @JsonProperty
    @Column(name = "isassigned", nullable = false, columnDefinition = "boolean default false")
    private boolean isAssigned;

    @Transient
    private String name;

    @Column(length = 255)
    private String description;

    @JsonProperty
    @Column(name = "isenabled", nullable = false, columnDefinition = "boolean default false")
    private boolean isEnabled;

    @Column(name = "permissionorder", nullable = false, columnDefinition = "int default 0")
    private int permissionOrder;

}
