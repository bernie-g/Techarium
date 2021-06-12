package software.bernie.techarium.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.util.Map;
import java.util.WeakHashMap;

public class LogCache {
    private static Map<Class, Logger> cache = new WeakHashMap<>();

    public static Logger getLogger() {
        return getLogger(StackLocatorUtil.getCallerClass(3));
    }

    public static Logger getLogger(Class clazz) {
        if (cache.containsKey(clazz)) {
            return cache.get(clazz);
        }
        Logger logger = LogManager.getLogger(clazz);
        cache.put(clazz, logger);
        return logger;
    }

}
