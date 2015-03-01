package net.mlpnn.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lindes Roets
 */
public class CollectionsTest {

    @Test
    public void testShuffle() {

        List<Integer> numbers = getOneToTenList();
        List<Integer> numbers2 = getOneToTenList();

        Random r = new Random(1);
        Collections.shuffle(numbers, r);
        Collections.shuffle(numbers2, r);

        Assert.assertEquals(numbers.get(1), numbers2.get(1));

    }

    private List<Integer> getOneToTenList() {
        List<Integer> numbers = new ArrayList<>();

        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);
        numbers.add(8);
        numbers.add(9);
        numbers.add(10);
        return numbers;
    }

}
