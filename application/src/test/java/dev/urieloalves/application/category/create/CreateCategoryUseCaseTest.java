package dev.urieloalves.application.category.create;

import dev.urieloalves.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @Test
    public void givenValidCommand_whenCallCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());


        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var useCase = new DefaultCreateCategoryUseCase(gateway);
        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenInvalidName_whenCallCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(command).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenValidCommandWithInactiveCategory_whenCallCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        Mockito.when(gateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());


        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var useCase = new DefaultCreateCategoryUseCase(gateway);
        final var actualOutput = useCase.execute(command).get();

        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.nonNull(category.getDeletedAt())
                ));
    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        Mockito.when(gateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var notification = useCase.execute(command).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

        Mockito.verify(gateway, Mockito.times(1))
                .create(Mockito.argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }
}
