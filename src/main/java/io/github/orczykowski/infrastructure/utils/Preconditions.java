package io.github.orczykowski.infrastructure.utils;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Preconditions {

    public static void checkRequiredArgument(final Object o) {
        check(Objects::nonNull, o, IllegalArgumentException::new);
    }

    public static void checkNotBlankArgument(final String str) {
        check(Objects::nonNull, str, IllegalArgumentException::new);
        check(s -> !s.isBlank(), str, IllegalArgumentException::new);
    }

    public static <T> void checkState(final Predicate<T> predicate, final T value) {
        check(predicate, value, IllegalStateException::new);
    }

    public static <T> void check(final Predicate<T> predicate, final T value,
                                 final Supplier<? extends RuntimeException> exception) {
        if (!predicate.test(value)) {
            throw exception.get();
        }
    }
}
