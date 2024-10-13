package org.example;

import java.util.List;

public class Sorts {
    public List<Integer> sortList(List<Integer> list, int sortType) {
        boolean flag;
        do {
            flag = false;
            if (sortType == 1) {
                MergeSort mergeSort = new MergeSort(50);
                List<Integer> sortedList = mergeSort.sortMerge(list);
                return sortedList;
            } else if (sortType == 2) {
                BubbleSort bubbleSort = new BubbleSort(10);
                List<Integer> sortedList = bubbleSort.sortBubble(list);
                return sortedList;
            } else {
                System.err.println("Такого варианта алгоритма сортировки не существует.");
                flag = true;
            }
        } while (flag);

        return list;
    }
}
