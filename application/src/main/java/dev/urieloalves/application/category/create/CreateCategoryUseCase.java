package dev.urieloalves.application.category.create;

import dev.urieloalves.application.UseCase;
import dev.urieloalves.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
