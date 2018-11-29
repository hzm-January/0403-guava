package houzm.guava.cache.testapi;

import houzm.accumulation.guava.JsonUtil;
import houzm.guava.cache.testapi.collection.MakeCollection;
import houzm.guava.cache.testapi.collection.User;
import java.io.IOException;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Author: hzm_dream@163.com
 * Date:  2018/11/26 16:25
 * Modified By:
 * Description：
 */
public class GuavaCacheCollectionDemo {
    public static void main(String[] args) {
        Cache<Long, String> cache = CacheBuilder.newBuilder().build();
        cache.put(1111L, JsonUtil.objectToJson(MakeCollection.getList()));
        @Nullable String jsonData = cache.getIfPresent(1111L);
        System.out.println(jsonData);
//        System.out.println(JsonUtil.jsonToList(jsonData, User.class));
        List<User> users = JsonUtil.jsonToList(jsonData, User.class);
        List<User> lists = JsonUtil.jsonToObject(jsonData, new TypeReference<List<User>>() {});
//        for (User user : users) {
//            System.out.println(user);
//        }

        try {
            JavaType javaType = new ObjectMapper().getTypeFactory().constructParametricType(List.class, User.class);
            List<User> users1 = new ObjectMapper().readValue(jsonData, javaType);
            System.out.println(users1);

            JavaType javaType2 = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, User.class);
            List<User> users2 = new ObjectMapper().readValue(jsonData, javaType2);
            System.out.println(users2);

            List<User> users3 = new ObjectMapper().readValue(jsonData, new TypeReference<List<User>>(){});
            System.out.println(users3);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
