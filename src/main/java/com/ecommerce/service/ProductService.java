package com.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public Product save(Product p) { return repo.save(p); }

    public List<Product> findAll() { return repo.findAll(); }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void delete(Long id) { repo.deleteById(id); }
}
