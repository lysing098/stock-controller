package com.example.stockcontroller.service;

import com.example.stockcontroller.exception.MyResourceNotFoundException;
import com.example.stockcontroller.model.*;
import com.example.stockcontroller.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SupplyService {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<Supply> getAllSupply(int page, String search, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (search == null || search.isEmpty()) {
            return supplyRepository.findAll(pageable);
        }

        return supplyRepository.findByProduct_NameContainingIgnoreCase(search, pageable);
    }


    public Supply create(@Valid Supply supply) {

        Supplier supplier = supplierRepository.findById(supply.getSupplier().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("Supplier not found"));

        Product product = productRepository.findById(supply.getProduct().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("Product not found"));

        User user = userRepository.findById(supply.getUser().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("User not found"));

        Stock stock = stockRepository.findByProduct(product).orElse(new Stock());

        stock.setProduct(product);

        if (stock.getQuantity() == null) {
            stock.setQuantity(0);
        }

        stock.setQuantity(stock.getQuantity() + supply.getQuantity());

        stockRepository.save(stock);

        supply.setProduct(product);
        supply.setSupplier(supplier);
        supply.setUser(user);

        return supplyRepository.save(supply);
    }


    public Supply update(Long id, @Valid Supply supply) {

        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Supply not found"));

        Supplier supplier = supplierRepository.findById(supply.getSupplier().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("Supplier not found"));

        Product product = productRepository.findById(supply.getProduct().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("Product not found"));

        User user = userRepository.findById(supply.getUser().getId())
                .orElseThrow(() -> new MyResourceNotFoundException("User not found"));

        Stock stock = stockRepository.findByProduct(product)
                .orElseThrow(() -> new MyResourceNotFoundException("Stock not found"));

        int oldQty = existingSupply.getQuantity();
        int newQty = supply.getQuantity();

        int diff = newQty - oldQty;

        stock.setQuantity(stock.getQuantity() + diff);

        stockRepository.save(stock);

        existingSupply.setSupplier(supplier);
        existingSupply.setUser(user);
        existingSupply.setQuantity(newQty);
        existingSupply.setPrice(supply.getPrice());
        existingSupply.setSupplyDate(supply.getSupplyDate());
        existingSupply.setExpiryDate(supply.getExpiryDate());

        return supplyRepository.save(existingSupply);
    }


    public Supply delete(Long id) {

        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Supply not found"));

        Product product = supply.getProduct();

        Stock stock = stockRepository.findByProduct(product)
                .orElseThrow(() -> new MyResourceNotFoundException("Stock not found"));

        stock.setQuantity(stock.getQuantity() - supply.getQuantity());

        stockRepository.save(stock);

        supplyRepository.delete(supply);

        return supply;
    }
}