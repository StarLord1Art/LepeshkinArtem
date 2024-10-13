package org.example;

import java.util.List;

public class BubbleSort {
    private int maxSize;

    public BubbleSort(int maxSize) {
        this.maxSize = maxSize;
    }

    public List<Integer> sortBubble(List<Integer> list) throws IllegalArgumentException {
        if (this.maxSize < list.size()) {
            throw new IllegalArgumentException(
                    "Превышен лимит количества элементов в списке для этого алгоритма сортировки.");
        }

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    int temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }

        return list;
    }
}
