package mrnavastar.sqlib.config;

import com.electronwill.nightconfig.core.CommentedConfig;

import java.lang.reflect.Field;

public class ConfigUtil {

    public static void parseComments(Object object, CommentedConfig config) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            System.out.println(field.getName());

            Object value = field.get(object);
            if (!value.getClass().isMemberClass()) continue;

            if (field.isAnnotationPresent(Comment.class)) {
                config.setComment(field.getName(), field.getAnnotation(Comment.class).value());
                System.out.println(field.getName());
            }

            parseComments(value, config);
        }
    }
}
