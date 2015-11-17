package com.yida.spider4j.crawler.utils.collection;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.yida.spider4j.crawler.utils.collection.base.Preconditions;

/**
 * This class provides a skeletal implementation of the {@link ListIterator}
 * interface across a fixed number of elements that may be retrieved by
 * position. It does not support {@link #remove}, {@link #set}, or {@link #add}.
 *
 * @author Jared Levy
 */
abstract class AbstractIndexedListIterator<E>
    extends UnmodifiableListIterator<E> {
  private final int size;
  private int position;

  /**
   * Returns the element with the specified index. This method is called by
   * {@link #next()}.
   */
  protected abstract E get(int index);

  /**
   * Constructs an iterator across a sequence of the given size whose initial
   * position is 0. That is, the first call to {@link #next()} will return the
   * first element (or throw {@link NoSuchElementException} if {@code size} is
   * zero).
   *
   * @throws IllegalArgumentException if {@code size} is negative
   */
  protected AbstractIndexedListIterator(int size) {
    this(size, 0);
  }

  /**
   * Constructs an iterator across a sequence of the given size with the given
   * initial position. That is, the first call to {@link #nextIndex()} will
   * return {@code position}, and the first call to {@link #next()} will return
   * the element at that index, if available. Calls to {@link #previous()} can
   * retrieve the preceding {@code position} elements.
   *
   * @throws IndexOutOfBoundsException if {@code index} is negative or is
   *         greater than {@code size}
   * @throws IllegalArgumentException if {@code size} is negative
   */
  protected AbstractIndexedListIterator(int size, int position) {
    Preconditions.checkPositionIndex(position, size);
    this.size = size;
    this.position = position;
  }

  public final boolean hasNext() {
    return position < size;
  }

  public final E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return get(position++);
  }

  public final int nextIndex() {
    return position;
  }

  public final boolean hasPrevious() {
    return position > 0;
  }

  public final E previous() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    return get(--position);
  }

  public final int previousIndex() {
    return position - 1;
  }
}
