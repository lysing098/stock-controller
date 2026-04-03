package com.example.stockcontroller.service;

import com.example.stockcontroller.config.FileImageService;
import com.example.stockcontroller.model.Supplier;
import com.example.stockcontroller.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private FileImageService fileImageService;

    public Page<Supplier> getAllSuppliers(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        if(search == null || search.isEmpty()) {
            return supplierRepository.findAll(pageable);
        }
        return supplierRepository.findByNameContainingIgnoreCase(search,pageable);
    }


    public Supplier create(@Valid String name, @Valid String tel, @Valid boolean telegram, @Valid String address, MultipartFile image) throws IOException {
        var imageName = fileImageService.uploadImage(image);

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setTel(tel);
        supplier.setTelegram(telegram);
        supplier.setAddress(address);
        supplier.setImage(imageName);
        return supplierRepository.save(supplier);
    }

    public Supplier update(Long id, @Valid String name, @Valid String tel, @Valid boolean telegram, @Valid String address, MultipartFile image) throws IOException {
        var supplier = supplierRepository.findById(id).orElseThrow(()-> new RuntimeException("Supplier not found with id: " + id));
        if(image != null && !image.isEmpty()) {
            if(supplier.getImage() != null && !supplier.getImage().isEmpty()) {
                fileImageService.deleteImage(supplier.getImage());
            }
            var newImage = fileImageService.uploadImage(image);
            supplier.setImage(newImage);
        }
        supplier.setName(name);
        supplier.setTel(tel);
        supplier.setTelegram(telegram);
        supplier.setAddress(address);

        return supplierRepository.save(supplier);
    }

    public Supplier delete(Long id) {
        var supplier = supplierRepository.findById(id).orElseThrow(()-> new RuntimeException("Supplier not found with id: " + id));
        if(supplier.getImage() != null && !supplier.getImage().isEmpty()) {
            fileImageService.deleteImage(supplier.getImage());
        }
        supplierRepository.delete(supplier);
        return supplier;
    }
}
