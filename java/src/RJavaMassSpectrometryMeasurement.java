
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-08-31
 */

public class RJavaMassSpectrometryMeasurement {
    private static List<List<Object>> listlist = new ArrayList<>();

    public static List<List<Object>> getListlist() {
        return listlist;
    }

    public static void setListlist(List<List<Object>> listlist) {
        RJavaMassSpectrometryMeasurement.listlist = listlist;
    }

    public static List<Object> putArgsIntoList(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) {
        List<Object> list = new ArrayList<>();

        list.add(typ);
        list.add(searchengine);
        list.add(mz);
        list.add(intensity);
        list.add(peptidmass);
        list.add(rt);
        list.add(chargestate);
        list.add(id);

        return list;
    }

    public static List<List<Object>> putListIntoList(List<Object> list) {
        List<List<Object>> ll = getListlist();
        ll.add(list);
        setListlist(ll);

        return getListlist();
    }
}
