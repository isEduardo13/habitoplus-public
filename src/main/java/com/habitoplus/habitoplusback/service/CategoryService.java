package com.habitoplus.habitoplusback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.exception.CategoryNotFoundException;
import com.habitoplus.habitoplusback.model.Category;
import com.habitoplus.habitoplusback.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category getById(Integer idCategory) {
        if (!categoryRepo.existsById(idCategory)) {
            throw new CategoryNotFoundException("");
        }
        return categoryRepo.findById(idCategory).get();
    }
}
