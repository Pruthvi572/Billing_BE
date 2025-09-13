package com.billing.Invoizo.initialsetup.dto;


import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitialSetupDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1759502337993452092L;

    private List<EmployeeEntity> employeeEntities;

    private ULBEntity ulbEntity;

    private List<ULBEntity> multiple;

    private boolean isForMultiple;

    // this is for admin users savimg
    private List<EmployeeEntity> admins;

    private List<ModulesEntity> moduleEntities;



}
