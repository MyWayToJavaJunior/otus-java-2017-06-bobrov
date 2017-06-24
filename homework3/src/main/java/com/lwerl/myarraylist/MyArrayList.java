package com.lwerl.myarraylist;

import java.util.*;

/**
 * Created by lWeRl on 17.06.2017.
 * Homework 3
 */
public class MyArrayList<E> implements List<E> {

    private static int DEFAULT_INITIAL_SIZE = 10;

    private Object[] array;

    private int size;

    public MyArrayList() {
        array = new Object[DEFAULT_INITIAL_SIZE];
    }

    public MyArrayList(int initSize) {
        if (initSize < 0) {
            throw new IllegalArgumentException("Wrong initial size: " + initSize);
        } else if (initSize <= 10) {
            array = new Object[DEFAULT_INITIAL_SIZE];
        } else {
            array = new Object[initSize];
        }
    }

    public MyArrayList(Collection<? extends E> collection) {
        array = collection.toArray();
        size = array.length;
        if (size == 0)
            array = new Object[DEFAULT_INITIAL_SIZE];
    }

    private void grow(int newSize) {
        if (newSize < 0) {
            throw new OutOfMemoryError();
        }
        int oldLength = array.length;
        int minLengthToGrow = (oldLength >> 1) + (oldLength >> 2);
        int newLength = oldLength + (oldLength >> 1);
        Object[] array;
        if (newSize > minLengthToGrow) {
            if (newLength < 0) {
                array = new Object[Integer.MAX_VALUE];
            } else {
                array = new Object[newLength];
            }
            System.arraycopy(this.array, 0, array, 0, this.size);
            this.array = array;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size - 1)
            throw new IndexOutOfBoundsException("Index: " + index);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            Object[] result = new Object[size];
            System.arraycopy(array, 0, result, 0, size);
            return (T[]) result;
        }
        System.arraycopy(array, 0, a, 0, size);
        return a;
    }

    public boolean add(E e) {
        grow(size + 1);
        array[size++] = e;
        return true;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            System.arraycopy(array, index + 1, array, index, size - index);
            array[--size] = null;
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        throw new RuntimeException();
    }

    public boolean addAll(Collection<? extends E> c) {
        Object[] addArray = c.toArray();
        int addSize = addArray.length;
        grow(size + addSize);
        System.arraycopy(addArray, 0, array, size, addSize);
        size += addSize;
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new RuntimeException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException();
    }

    public void clear() {
        size = 0;
        array = new Object[DEFAULT_INITIAL_SIZE];
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;
        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    public int hashCode() {
        int hashCode = 1;
        for (E e : this)
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        return hashCode;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) array[index];
    }

    public E set(int index, E element) {
        checkIndex(index);
        array[index] = element;
        return element;
    }

    public void add(int index, E element) {
        checkIndexForAdd(index);
        grow(size + 1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E element = (E) array[index];
        System.arraycopy(array, index + 1, array, index, size - index);
        return element;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null)
                    return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(o))
                    return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        throw new RuntimeException();
    }

    public ListIterator<E> listIterator() {
        return new MyListIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return new MyListIterator(index);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        throw new RuntimeException();
    }

    private class MyListIterator implements ListIterator<E> {

        int cursor;
        int lastRet = -1;
        boolean operation;

        private MyListIterator(int cursor) {
            this.cursor = cursor;
        }

        private MyListIterator() {
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (cursor >= size)
                throw new NoSuchElementException();
            lastRet = cursor;
            operation = true;
            return (E) array[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor - 1 != -1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E previous() {
            --cursor;
            if (cursor < 0)
                throw new NoSuchElementException();
            lastRet = cursor;
            operation = true;
            return (E) array[cursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (operation) {
                operation = false;
                MyArrayList.this.remove(lastRet);
            }
        }

        @Override
        public void set(E e) {
            if (operation) {
                MyArrayList.this.set(lastRet, e);
            }
        }

        @Override
        public void add(E e) {
            throw new RuntimeException();
        }
    }
}
