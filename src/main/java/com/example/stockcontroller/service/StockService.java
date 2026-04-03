package com.example.stockcontroller.service;

import com.example.stockcontroller.model.Stock;
import com.example.stockcontroller.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Page<Stock> getAllStocks(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(search == null || search.isEmpty()) {
            return stockRepository.findAll(pageable);
        }

        return stockRepository.findByProduct_NameContainingIgnoreCase(search, pageable);
    }
}
