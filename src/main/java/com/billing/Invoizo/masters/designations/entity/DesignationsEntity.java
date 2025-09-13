package com.billing.Invoizo.masters.designations.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;

@Entity
@Table(name = "master_designations")
@Data
public class DesignationsEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    // id of the designation (PK)
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "int default 0")
    private int id;

    // Name of the Designation
    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "abbreviation", length = 50)
    private String abbreviation;

    @Column(name = "hierarchy", nullable = false, columnDefinition = "int default 0")
    private int hierarchy;

    @Column(name = "designationorder", nullable = false, columnDefinition = "int default 0")
    private int designationOrder;


}
