package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;


import edu.uoc.epcsd.productcatalog.domain.Category;
import edu.uoc.epcsd.productcatalog.domain.repository.CategoryRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SpringDataCategoryRepository jpaRepository;

    public CategoryRepositoryImpl(SpringDataCategoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Category> findAllCategories() {
        return jpaRepository.findAll().stream()
                .map(CategoryEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return jpaRepository.findById(id).map(CategoryEntity::toDomain);
    }

    public List<Category> findCategoriesByExample(Category category) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", contains().ignoreCase())
                .withMatcher("description", contains().ignoreCase());


        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        if (category.getParentId() != null) {
            categoryEntity.setParent(jpaRepository.findById((category.getParentId())).orElseThrow(IllegalArgumentException::new));
        }

        Example<CategoryEntity> example = Example.of(categoryEntity, matcher);

        List<CategoryEntity> categoryEntities = jpaRepository.findAll(example);
        Stream<CategoryEntity> categoryEntityStream = categoryEntities.stream();

        return categoryEntityStream
                .map(CategoryEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long createCategory(Category category) {

        CategoryEntity parentCategory = null;
        if (category.getParentId() != null) {
            parentCategory = jpaRepository.findById(category.getParentId()).orElseThrow(IllegalArgumentException::new);
        }

        CategoryEntity categoryEntity = CategoryEntity.fromDomain(category);
        categoryEntity.setParent(parentCategory);

        return jpaRepository.save(categoryEntity).getId();
    }
}
