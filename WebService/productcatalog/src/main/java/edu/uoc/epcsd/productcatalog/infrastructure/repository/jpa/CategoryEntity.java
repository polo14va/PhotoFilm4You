package edu.uoc.epcsd.productcatalog.infrastructure.repository.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uoc.epcsd.productcatalog.domain.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "Category")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class CategoryEntity extends CatalogElement implements DomainTranslatable<Category> {

    @ManyToOne
    private CategoryEntity parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<CategoryEntity> children;

    public static CategoryEntity fromDomain(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryEntity.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public Category toDomain() {
        return Category.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .parentId(this.getParent() != null ? this.getParent().getId() : null)
                .build();
    }
}
