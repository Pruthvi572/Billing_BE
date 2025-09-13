package com.billing.Invoizo.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaginationResponse<T> implements Serializable {

    private List<T> data;
    private long totalRecords;
}
