package dev.urieloalves.domain.category;

import dev.urieloalves.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {

    private final String value;

    private CategoryId(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryId unique() {
        return CategoryId.from(UUID.randomUUID());
    }

    public static CategoryId from(final String id) {
        return new CategoryId(id);
    }

    public static CategoryId from(final UUID id) {
        return new CategoryId(id.toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryId that = (CategoryId) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
