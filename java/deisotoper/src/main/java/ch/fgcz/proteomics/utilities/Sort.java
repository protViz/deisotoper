package ch.fgcz.proteomics.utilities;

/**
 * @author Lucas Schmidt
 * @since 2017-10-09
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sort {
    public static <T extends Comparable<T>> void keySort(final List<T> key, List<?>... lists) {
        List<Integer> indicearray = new ArrayList<Integer>();
        for (int i = 0; i < key.size(); i++)
            indicearray.add(i);

        Collections.sort(indicearray, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                return key.get(i).compareTo(key.get(j));
            }
        });

        Map<Integer, Integer> swap = new HashMap<Integer, Integer>(indicearray.size());

        for (int i = 0; i < indicearray.size(); i++) {
            int k = indicearray.get(i);
            while (swap.containsKey(k))
                k = swap.get(k);

            swap.put(i, k);
        }

        for (Map.Entry<Integer, Integer> e : swap.entrySet())
            for (List<?> list : lists)
                Collections.swap(list, e.getKey(), e.getValue());
    }
}
