package dev.urieloalves.domain.category;

import dev.urieloalves.domain.exceptions.DomainException;
import dev.urieloalves.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallingNewCategory_thenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenInvalidEmptyName_whenCallingNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameLengthLessThan3_whenCallingNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "fi ";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameLengthMoreThan255_whenCallingNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = """
                O empenho em analisar a percepção das dificuldades é um ativo de TI de alternativas aos aplicativos\s
                convencionais. A implantação, na prática, prova que a utilização de recursos de hardware dedicados\s
                implica na melhor utilização dos links de dados dos requi
                """;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenValidEmptyDescription_whenCallingNewCategoryAndValidate_thenShouldReceiveOk() {
        final String expectedName = "Movies";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidFalseIsActive_whenCallingNewCategoryAndValidate_thenShouldReceiveOk() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidActiveCategory_whenCallDeactivate_thenReturnInactiveCategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualCategory = category.deactivate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidInactiveCategory_whenCallActivate_thenReturnActiveCategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var actualCategory = category.activate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdate_thenReturnUpdatedCategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Moviess", "The most watched categoryy", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateToInactive_thenReturnInactiveCategory() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateWithInvalidParams_thenReturnUpdatedCategory() {
        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movies", expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
