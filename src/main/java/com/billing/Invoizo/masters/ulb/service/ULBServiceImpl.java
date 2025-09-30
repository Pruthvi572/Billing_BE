package com.billing.Invoizo.masters.ulb.service;

import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import org.hibernate.persister.collection.mutation.RowMutationOperations;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ULBServiceImpl implements ULBService {





//    @Override
//    public JSONObject saveUlb(ULBEntity ulbEntity, Integer designationId, String ulbCode) {
//        JSONObject jsonObject = new JSONObject();
//        Date date = new Date();
//        Long ulbNameCount = (Long) sessionFactory.getCurrentSession().createCriteria(ULBEntity.class)
//                .setProjection(Projections.rowCount())
//                .add(RowMutationOperations.Restrictions.eq("name", ulbEntity.getName())).uniqueResult();
//        ulbEntity.setModifiedAt(date);
//        if (ulbNameCount.intValue() != 0) {
//            jsonObject.put("alert", 3);
//        } else {
//            ulbEntity.setCreatedAt(date);
//            Map<String, Object> distinct = distinctLicenceAndSubclient();
//            ulbEntity.setSubClientId((String) distinct.get("subClientId"));
//            ulbEntity.setLicenseId((String) distinct.get("licenseId"));
//        }
//        return adminEmployee(ulbEntity, designationId, ulbCode);
//    }
}
