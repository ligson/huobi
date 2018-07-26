
package org.ligson.huobi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/***
 * JSON序列化处理工具
 */
public class JsonUtil {

    public static ObjectMapper objectMapper;

    private static ObjectMapper mapper() {
        if (objectMapper == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //不显示为null的字段
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
            //mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            objectMapper = mapper;
        }
        return objectMapper;
    }


    /***
     * 把对象序列化成json字符串
     * @param object 对象
     * @return json字符串
     */
    public static String toJson(Object object) {
        try {
            return mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 将字符串json转成clazz类型的对象
     * @param json json字符串
     * @param clazz 返回对象类型
     * @param <T> 返回对象类型
     * @return 返回对象
     */
    public static <T> T readObject(String json, Class<T> clazz) {
        try {
            return mapper().readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 将字符串json转成clazz类型的对象
     * @param json json字符串
     * @param clazz 返回对象类型
     * @param <T> 返回对象类型
     * @return 返回对象
     */
    public static <T> List<T> readArray(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = mapper();
        try {
            JsonNode node = mapper.readTree(json);

            if (node.isArray()) {
                ArrayNode arrayNode = (ArrayNode) node;
                for (JsonNode jsonNode : arrayNode) {
                    list.add(mapper.readValue(jsonNode.traverse(), clazz));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /***
     * 将字符串json转成clazz类型的对象
     * @param json json字符串
     * @param clazz 返回对象类型
     * @param <T> 返回对象类型
     * @return 返回对象
     */
    public static <T> T readObject(InputStream json, Class<T> clazz) {
        try {
            return mapper().readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 复杂对象转换，以后可能废弃
     * @param o
     * @return
     */
    public static JsonNode fromObject(Object o) {
        String json = toJson(o);
        try {
            return mapper().readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * json串转node
     * @param json
     * @return
     */
    public static JsonNode readNode(String json) {
        try {
            return mapper().readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
