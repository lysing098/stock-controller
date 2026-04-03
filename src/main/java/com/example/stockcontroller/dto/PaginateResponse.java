package com.example.stockcontroller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PaginateResponse<T> {

    private List<T> data;
    private int currentPage;
    private int perPage;
    private long total;
    private int totalPages;
    private boolean firstPage;
    private boolean lastPage;

}
