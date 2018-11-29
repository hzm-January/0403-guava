package houzm.guava.cache.testapi.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/26 16:36
 * Modified By:
 * Description：
 */
public class MakeCollection {

    private static List list;

    static {
        list = new ArrayList(10);
        list.add(new User("houzm", "12"));
        list.add(new User("zfsaf", "12"));
        list.add(new User("acxcsd", "12"));
        list.add(new User("ghfgh", "12"));
        list.add(new User("ert", "12"));
    }

    public static List getList() {
        return list;
    }

}
