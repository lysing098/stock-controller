package com.example.stockcontroller.service;

import com.example.stockcontroller.config.FileImageService;
import com.example.stockcontroller.dto.PaginateResponse;
import com.example.stockcontroller.exception.MyResourceNotFoundException;
import com.example.stockcontroller.model.Brand;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.repository.BrandRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private FileImageService fileImageService;


    public Page<Brand> getAllBrands(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if(search == null || search.isEmpty()) {
            return brandRepository.findAll(pageable);
        }
        return brandRepository.findByNameContainingIgnoreCase(search,pageable);
    }


    public Brand create(@Valid String name, MultipartFile image) throws IOException {
        String imageName = fileImageService.uploadImage(image);

        Brand brand = new Brand();
        brand.setName(name);
        brand.setImage(imageName);

        return brandRepository.save(brand);

    }

    public Brand update(Long id, @Valid String name, MultipartFile image) throws IOException {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Brand not found with id " + id));

        // Update name
        brand.setName(name);

        // If new image is provided
        if (image != null && !image.isEmpty()) {
            // Delete old image if it exists
            if (brand.getImage() != null && !brand.getImage().isEmpty()) {
                fileImageService.deleteImage(brand.getImage());
            }

            // Upload new image
            String newImage = fileImageService.uploadImage(image);
            brand.setImage(newImage);
        }

        return brandRepository.save(brand);
    }

    public Brand delete(Long id) {
        var brand  = brandRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Brand not found with id " + id));
        if(brand.getImage() != null && !brand.getImage().isEmpty()) {
            fileImageService.deleteImage(brand.getImage());
        }
        brandRepository.delete(brand);
        return brand;
    }
}
