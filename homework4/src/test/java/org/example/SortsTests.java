package org.example;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortsTests {

    @Test
    public void TestSortType1() {
        Sorts sorts = new Sorts();
        List<Integer> list = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2);
        int sortType = 1;
        List<Integer> sorted = sorts.sortList(list, sortType);
        assertEquals(sorted, Arrays.asList(-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    @Test
    public void TestSortType2() {
        Sorts sorts = new Sorts();
        List<Integer> list = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        int sortType = 2;
        List<Integer> sorted = sorts.sortList(list, sortType);
        assertEquals(sorted, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    @Test
    public void TestMaxSize() {
        Sorts sorts = new Sorts();
        List<Integer> list = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 11, 12, 15, 17);
        int sortType = 2;
        assertThrows(IllegalArgumentException.class, () -> sorts.sortList(list, sortType));
    }

}
