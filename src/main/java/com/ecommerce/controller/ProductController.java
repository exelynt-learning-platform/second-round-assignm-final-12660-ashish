package com.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public Product create(@RequestBody Product p) { return service.save(p); }

    @GetMapping
    public List<Product> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted";
    }
}
