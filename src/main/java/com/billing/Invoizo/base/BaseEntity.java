package com.billing.Invoizo.base;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {


    @Column(length = 255, name = "createdby")
    private String createdBy;
    @Column(length = 255, name = "modifiedby")
    private String modifiedBy;

    @Column(name = "createdat")
    private Date createdAt;

    @Column(name = "modifiedat")
    private Date modifiedAt;

    @Column(nullable = false, columnDefinition = "boolean default false", name = "isdeleted")
    private boolean isDeleted;

}
