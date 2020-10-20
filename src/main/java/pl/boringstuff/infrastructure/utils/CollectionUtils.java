package pl.boringstuff.infrastructure.utils;

import java.util.Collection;
import java.util.Objects;

public class CollectionUtils {
  public static <T extends Collection> T firstNotNullOrEmpty(T first, T second) {
    if (Objects.isNull(second)) {
      throw new NullPointerException();
    }
    if (second.isEmpty()) {
      throw new IllegalArgumentException("second collection cannot be empty");
    }
    if (isNullOrEmpty(first)) {
      return second;
    }
    return first;
  }

  public static <T extends Collection> boolean isNullOrEmpty(final T first) {
    return Objects.isNull(first) || first.isEmpty();
  }


}
