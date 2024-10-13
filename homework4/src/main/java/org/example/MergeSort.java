package org.example;

import java.util.Collections;
import java.util.List;

public class MergeSort {
    private int maxSize;

    public MergeSort(int maxSize) {
        this.maxSize = maxSize;
    }

    public List<Integer> sortMerge(List<Integer> list) throws IllegalArgumentException {
        if (this.maxSize < list.size()) {
            throw new IllegalArgumentException(
                    "Превышен лимит количества элементов в списке для этого алгоритма сортировки.");
        }

        Collections.sort(list);
        return list;
    }
}
