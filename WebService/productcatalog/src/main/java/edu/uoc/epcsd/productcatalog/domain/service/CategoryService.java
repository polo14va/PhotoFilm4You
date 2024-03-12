package edu.uoc.epcsd.productcatalog.domain.service;


import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryService  {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }


    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }


    public List<Category> findCategoriesByExample(Category category) {
        return categoryRepository.findCategoriesByExample(category);
    }


    public Long createCategory(Category category) {
        return categoryRepository.createCategory(category);
    }
}
