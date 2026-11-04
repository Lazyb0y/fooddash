package com.fooddash.restaurant;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Base64;

/** An opaque cursor for keyset pagination: the position of the last row on the previous page. */
final class Cursor {

  private static final String PREFIX = "after:";

  private Cursor() {}

  static String encode(long lastId) {
    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString((PREFIX + lastId).getBytes(UTF_8));
  }

  static long decode(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return 0;
    }
    try {
      String decoded = new String(Base64.getUrlDecoder().decode(cursor), UTF_8);
      if (!decoded.startsWith(PREFIX)) {
        throw new IllegalArgumentException(decoded);
      }
      return Long.parseLong(decoded.substring(PREFIX.length()));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "The cursor is not valid; use the nextCursor from the previous page.");
    }
  }
}
