package remove_test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author chenk
 * @Date 2019/3/28  10:33
 **/

public class RemoveTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("2");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("2".equals(item)) {
                iterator.remove();
            }
        }
//        for (String item : list) {
//            if ("2".equals(item)) {
//                list.remove(item);
//            }
//        }
        System.out.println(list.toString());
    }
}
