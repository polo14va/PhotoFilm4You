package edu.uoc.epcsd.productcatalog.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends CatalogElement {

    private Long parentId;

}
