package com.walm.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>CollectionUtils</p>
 *
 * @author wangjn
 * @since 2019-12-10
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 排序
     *
     * @param list
     * @param comparator
     * @param <T>
     */
    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low >= high) {
            return;
        }

        T e = list.get(low);
        int left = low;
        int right = high;

        while (left < right) {

            while (left < right && comparator.compare(list.get(right), e) >= 0) {
                right --;
            }

            while (left < right && comparator.compare(list.get(left), e) <= 0) {
                left ++;
            }

            T temp = list.get(left);
            list.set(left, list.get(right));
            list.set(right, temp);
        }

        list.set(low, list.get(left));
        list.set(left, e);
        quickSort(list, low, left - 1, comparator);
        quickSort(list, left + 1, high, comparator);
    }

    @FunctionalInterface
    interface Comparator<T> {
        int compare(T t1, T t2);
    }
}
