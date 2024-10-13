package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Введите количество элементов в списке: ");
        int n = input.nextInt();

        List<Integer> list = new ArrayList<>();
        System.out.println("Введите элементы списка через пробел: ");
        for (int i = 0; i < n; i++) {
            list.add(input.nextInt());
        }

        System.out.println("Выберите тип сортировки (1 - сортировка слиянием; 2 - сортировка пузырьком): ");
        int sortType = input.nextInt();

        Sorts sorts = new Sorts();
        List<Integer> sortedList = sorts.sortList(list, sortType);
        System.out.println(sortedList);
    }
}
