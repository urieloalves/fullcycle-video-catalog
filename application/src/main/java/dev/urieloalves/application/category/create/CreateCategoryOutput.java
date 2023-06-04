package dev.urieloalves.application.category.create;

import dev.urieloalves.domain.category.Category;
import dev.urieloalves.domain.category.CategoryId;

public record CreateCategoryOutput(
        CategoryId id
) {

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
