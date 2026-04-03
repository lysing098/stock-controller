package com.example.stockcontroller.service;

import com.example.stockcontroller.config.FileImageService;
import com.example.stockcontroller.model.Brand;
import com.example.stockcontroller.model.Category;
import com.example.stockcontroller.model.Product;
import com.example.stockcontroller.model.User;
import com.example.stockcontroller.repository.BrandRepository;
import com.example.stockcontroller.repository.CategoryRepository;
import com.example.stockcontroller.repository.ProductRepository;
import com.example.stockcontroller.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileImageService fileImageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Product> getAllProducts(String search, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        if(search == null || search.isEmpty()) {
            return productRepository.findAll(pageable);
        }

        return productRepository.findByNameContainingIgnoreCase(search,pageable);
    }

    public Product create(@Valid String name, @Valid String code, @Valid double price, @Valid MultipartFile image, @Valid Category category, @Valid Brand brand, @Valid User user) throws IOException {
        var fileName = fileImageService.uploadImage(image);

        Category findCategory = categoryRepository.findById(category.getId()).orElseThrow(()-> new RuntimeException("Category not found with id " + category.getId()));

        Brand findBrand  = brandRepository.findById(brand.getId()).orElseThrow(()-> new RuntimeException("Brand not found with id " + brand.getId()));

        User findUser = userRepository.findById(user.getId()).orElseThrow(()-> new RuntimeException("User not found with id " + user.getId()));

        Product product = new Product();
        product.setName(name);
        product.setCode(code);
        product.setPrice(price);
        product.setImage(fileName);
        product.setCategory(findCategory);
        product.setBrand(findBrand);
        product.setUser(findUser);
        productRepository.save(product);
        return product;

    }

    public Product update(Long id, @Valid String name, @Valid String code, @Valid double price, @Valid MultipartFile image, @Valid Category category, @Valid Brand brand, @Valid User user) throws IOException {

        Product findProduct = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found with id " + id));

        Category findCategory = categoryRepository.findById(category.getId()).orElseThrow(()-> new RuntimeException("Category not found with id " + category.getId()));

        Brand findBrand  = brandRepository.findById(brand.getId()).orElseThrow(()-> new RuntimeException("Brand not found with id " + brand.getId()));

        User findUser = userRepository.findById(user.getId()).orElseThrow(()-> new RuntimeException("User not found with id " + user.getId()));

        if(image != null &&  !image.getOriginalFilename().isEmpty()) {

            if(findProduct.getImage() != null  && !findProduct.getImage().isEmpty()) {
                fileImageService.deleteImage(findProduct.getImage());

            }

            var fileName = fileImageService.uploadImage(image);
            findProduct.setImage(fileName);
        }

        findProduct.setName(name);
        findProduct.setCode(code);
        findProduct.setPrice(price);
        findProduct.setCategory(findCategory);
        findProduct.setBrand(findBrand);
        findProduct.setUser(findUser);

        productRepository.save(findProduct);
        return findProduct;

    }

    public Product delete(Long id) {
        Product findProduct = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found with id " + id));
        if(findProduct.getImage() != null  && !findProduct.getImage().isEmpty()) {
            fileImageService.deleteImage(findProduct.getImage());

        }
        productRepository.delete(findProduct);
        return findProduct;
    }
}
