package com.yida.spider4j.crawler.utils.collection.primitives;

import java.util.Comparator;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;
import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * Static utility methods pertaining to {@code byte} primitives that
 * interpret values as signed. The corresponding methods that treat the values
 * as unsigned are found in {@link UnsignedBytes}, and the methods for which
 * signedness is not an issue are in {@link Bytes}.
 *
 * @author Kevin Bourrillion
 * @since 1
 */
@GwtCompatible
public final class SignedBytes {
  private SignedBytes() {}

  /**
   * Returns the {@code byte} value that is equal to {@code value}, if possible.
   *
   * @param value any value in the range of the {@code byte} type
   * @return the {@code byte} value that equals {@code value}
   * @throws IllegalArgumentException if {@code value} is greater than {@link
   *     Byte#MAX_VALUE} or less than {@link Byte#MIN_VALUE}
   */
  public static byte checkedCast(long value) {
    byte result = (byte) value;
    Preconditions.checkArgument(result == value, "Out of range: %s", value);
    return result;
  }

  /**
   * Returns the {@code byte} nearest in value to {@code value}.
   *
   * @param value any {@code long} value
   * @return the same value cast to {@code byte} if it is in the range of the
   *     {@code byte} type, {@link Byte#MAX_VALUE} if it is too large,
   *     or {@link Byte#MIN_VALUE} if it is too small
   */
  public static byte saturatedCast(long value) {
    if (value > Byte.MAX_VALUE) {
      return Byte.MAX_VALUE;
    }
    if (value < Byte.MIN_VALUE) {
      return Byte.MIN_VALUE;
    }
    return (byte) value;
  }

  /**
   * Compares the two specified {@code byte} values. The sign of the value
   * returned is the same as that of {@code ((Byte) a).compareTo(b)}.
   *
   * @param a the first {@code byte} to compare
   * @param b the second {@code byte} to compare
   * @return a negative value if {@code a} is less than {@code b}; a positive
   *     value if {@code a} is greater than {@code b}; or zero if they are equal
   */
  public static int compare(byte a, byte b) {
    return a - b; // safe due to restricted range
  }

  /**
   * Returns the least value present in {@code array}.
   *
   * @param array a <i>nonempty</i> array of {@code byte} values
   * @return the value present in {@code array} that is less than or equal to
   *     every other value in the array
   * @throws IllegalArgumentException if {@code array} is empty
   */
  public static byte min(byte... array) {
    Preconditions.checkArgument(array.length > 0);
    byte min = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] < min) {
        min = array[i];
      }
    }
    return min;
  }

  /**
   * Returns the greatest value present in {@code array}.
   *
   * @param array a <i>nonempty</i> array of {@code byte} values
   * @return the value present in {@code array} that is greater than or equal to
   *     every other value in the array
   * @throws IllegalArgumentException if {@code array} is empty
   */
  public static byte max(byte... array) {
    Preconditions.checkArgument(array.length > 0);
    byte max = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] > max) {
        max = array[i];
      }
    }
    return max;
  }

  /**
   * Returns a string containing the supplied {@code byte} values separated
   * by {@code separator}. For example, {@code join(":", 0x01, 0x02, -0x01)}
   * returns the string {@code "1:2:-1"}.
   *
   * @param separator the text that should appear between consecutive values in
   *     the resulting string (but not at the start or end)
   * @param array an array of {@code byte} values, possibly empty
   */
  public static String join(String separator, byte... array) {
    Preconditions.checkNotNull(separator);
    if (array.length == 0) {
      return "";
    }

    // For pre-sizing a builder, just get the right order of magnitude
    StringBuilder builder = new StringBuilder(array.length * 5);
    builder.append(array[0]);
    for (int i = 1; i < array.length; i++) {
      builder.append(separator).append(array[i]);
    }
    return builder.toString();
  }

  /**
   * Returns a comparator that compares two {@code byte} arrays
   * lexicographically. That is, it compares, using {@link
   * #compare(byte, byte)}), the first pair of values that follow any common
   * prefix, or when one array is a prefix of the other, treats the shorter
   * array as the lesser. For example, {@code [] < [0x01] < [0x01, 0x80] <
   * [0x01, 0x7F] < [0x02]}. Values are treated as signed.
   *
   * <p>The returned comparator is inconsistent with {@link
   * Object#equals(Object)} (since arrays support only identity equality), but
   * it is consistent with {@link java.util.Arrays#equals(byte[], byte[])}.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Lexicographical_order">
   *     Lexicographical order article at Wikipedia</a>
   * @since 2
   */
  public static Comparator<byte[]> lexicographicalComparator() {
    return LexicographicalComparator.INSTANCE;
  }

  private enum LexicographicalComparator implements Comparator<byte[]> {
    INSTANCE;

    public int compare(byte[] left, byte[] right) {
      int minLength = Math.min(left.length, right.length);
      for (int i = 0; i < minLength; i++) {
        int result = SignedBytes.compare(left[i], right[i]);
        if (result != 0) {
          return result;
        }
      }
      return left.length - right.length;
    }
  }
}
