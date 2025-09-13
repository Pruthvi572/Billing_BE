package com.billing.Invoizo.masters.modules.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "setup_modules")
@Data
public class ModulesEntity implements Serializable {
    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The id.
     */
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "int default 0")
    private int id;

    /**
     * The module name.
     */
    @Column(name = "modulename", length = 255)
    private String moduleName;

    /**
     * The constant.
     */
    @Column(name = "constant", length = 255)
    private String constant;

    /**
     * The is disabled.
     */
    @JsonProperty
    @Column(name = "isdisabled", nullable = false, columnDefinition = "boolean default false")
    private boolean isDisabled;

    /**
     * The can be disabled.
     */
    //Used for showing in the UI
    @JsonProperty
    @Column(name = "canbedisabled", nullable = false, columnDefinition = "boolean default false")
    private boolean canBeDisabled;

    /**
     * The module description.
     */
    @Column(name = "moduledescription", length = 1000)
    private String moduleDescription;

    /**
     * The module image.
     */
    @Column(name = "moduleimage", length = 100)
    private String moduleImage;

    /**
     * The has approval flow.
     */
    @Column(name = "hasapprovalflow", nullable = false, columnDefinition = "boolean default false")
    @JsonProperty
    private boolean hasApprovalFlow;

    /**
     * The can be disabled flow.
     */
    @Column(name = "canbedisabledflow", nullable = false, columnDefinition = "boolean default false")
    @JsonProperty
    private boolean canBeDisabledFlow;

    /**
     * The is approval flow enabled.
     */
    @Column(name = "isapprovalflowenabled", nullable = false, columnDefinition = "boolean default false")
    @JsonProperty
    private boolean isApprovalFlowEnabled;

    @Column(name = "approvaltype", nullable = false, columnDefinition = "int default 0")
    private byte approvalType;
}
