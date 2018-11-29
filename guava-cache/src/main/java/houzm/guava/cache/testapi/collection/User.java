package houzm.guava.cache.testapi.collection;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/26 16:40
 * Modified By:
 * Description：
 */
public class User {
    private String name;
    private String age;

    public User() {
    }

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
