package dev.urieloalves.domain.validation.handler;

import dev.urieloalves.domain.exceptions.DomainException;
import dev.urieloalves.domain.validation.Error;
import dev.urieloalves.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation validation) {
        try {
            validation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
