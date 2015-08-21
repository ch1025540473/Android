package com.wezebra.zebraking.http;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * User: superalex
 * Date: 2015/1/22
 * Time: 16:25
 */
public class JacksonUtils
{
    private static final ObjectMapper mapper = new ObjectMapper();

    private JacksonUtils()
    {

    }

    public static ObjectMapper getInstance()
    {
        return mapper;
    }

    public static <T> T readJson(String jsonStr, Class<?> parametrized, Class<?>... elementClasses) throws Exception
    {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(parametrized, elementClasses);

        return mapper.readValue(jsonStr, javaType);

    }

    public static <T> T readListJson(String jsonStr, Class<?> parametrized,Class<?> collectionClass, Class<?>... elementClasses) throws Exception
    {
        JavaType javaListType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(parametrized, javaListType);

        return mapper.readValue(jsonStr, javaType);

    }
}
