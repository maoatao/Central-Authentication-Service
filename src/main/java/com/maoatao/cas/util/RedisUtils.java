package com.maoatao.cas.util;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作工具
 *
 * @author MaoAtao
 * @date 2021-05-21 16:24:01
 */
@Slf4j
public abstract class RedisUtils {

    /**
     * redis key 分隔符
     */
    public static final String REDIS_KEY = ":";

    /*------------------------------- String Object 开始 -------------------------------*/

    private static final RedisTemplate<String, String> STRING_REDIS_TEMPLATE = SpringContextUtils.getBean("stringRedisTemplate", StringRedisTemplate.class);
    @SuppressWarnings("unchecked")
    private static final RedisTemplate<String, Object> OBJECT_REDIS_TEMPLATE = SpringContextUtils.getBean("objectRedisTemplate", RedisTemplate.class);

    /**
     * 写入Redis
     *
     * @param key   key
     * @param value 值
     * @return 操作结果
     */
    public static boolean set(@NonNull final String key, String value) {
        boolean result = false;
        try {
            STRING_REDIS_TEMPLATE.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("写入Redis失败!", e);
        }
        return result;
    }

    /**
     * 写入Redis
     *
     * @param key   key
     * @param value 值
     * @param time  过期时间
     * @return 操作结果
     */
    public static boolean set(@NonNull final String key, String value, long time) {
        boolean result = false;
        try {
            STRING_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("写入Redis失败!", e);
        }
        return result;
    }

    /**
     * 获取字符串
     *
     * @param key key
     * @return 字符串的值
     */
    public static String getString(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                return STRING_REDIS_TEMPLATE.opsForValue().get(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return "";
    }

    /**
     * 写入Redis
     *
     * @param key   key
     * @param value 值
     * @return 操作结果
     */
    public static boolean set(@NonNull final String key, Object value) {
        boolean result = false;
        try {
            OBJECT_REDIS_TEMPLATE.opsForValue().set(key, value);
            result = true;
        } catch (Exception e) {
            log.error("写入Redis失败!", e);
        }
        return result;
    }

    /**
     * 写入Redis
     *
     * @param key   key
     * @param value 值
     * @param time  过期时间
     * @return 操作结果
     */
    public static boolean set(@NonNull final String key, Object value, long time) {
        boolean result = false;
        try {
            OBJECT_REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("写入Redis失败!", e);
        }
        return result;
    }

    /**
     * 获取对象
     *
     * @param key key
     * @return 对象
     */
    public static Object getObject(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(OBJECT_REDIS_TEMPLATE.hasKey(key))) {
                return OBJECT_REDIS_TEMPLATE.opsForValue().get(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return null;
    }

    /**
     * 获取对象集合
     *
     * @param key key
     * @return 对象集合
     */
    public static Long add(@NonNull final String key, String... values) {
        Long size = null;
        try {
            size = STRING_REDIS_TEMPLATE.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("添加Redis Set失败!", e);
        }
        return size;
    }

    /**
     * 获取对象集合
     *
     * @param key key
     * @return 对象集合
     */
    public static Set<String> getStringSet(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                return STRING_REDIS_TEMPLATE.opsForSet().members(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return null;
    }

    /**
     * 随机获取集合一个元素
     *
     * @param key key
     * @return 对象集合
     */
    public static String popString(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                return STRING_REDIS_TEMPLATE.opsForSet().pop(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return null;
    }

    /**
     * 通过key删除set中的值
     *
     * @param key key
     * @return 删除结果
     */
    public static Long remove(@NonNull final String key, String... values) {
        Long size = null;
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                size = STRING_REDIS_TEMPLATE.opsForSet().remove(key, values);
            }
        } catch (Exception e) {
            log.error("删除Redis失败!", e);
        }
        return size;
    }

    /**
     * 获取对象集合
     *
     * @param key key
     * @return 对象集合
     */
    public static Long add(@NonNull final String key, Object... values) {
        Long size = null;
        try {
            size = OBJECT_REDIS_TEMPLATE.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("添加Redis Set失败!", e);
        }
        return size;
    }

    /**
     * 获取对象集合
     *
     * @param key key
     * @return 对象集合
     */
    public static Set<Object> getObjectSet(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(OBJECT_REDIS_TEMPLATE.hasKey(key))) {
                return OBJECT_REDIS_TEMPLATE.opsForSet().members(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return null;
    }

    /**
     * 随机获取集合一个元素
     *
     * @param key key
     * @return 对象集合
     */
    public static Object popObject(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(OBJECT_REDIS_TEMPLATE.hasKey(key))) {
                return OBJECT_REDIS_TEMPLATE.opsForSet().pop(key);
            }
        } catch (Exception e) {
            log.error("读取Redis失败!", e);
        }
        return null;
    }

    /**
     * 通过key删除
     *
     * @param key key
     * @return 删除结果
     */
    public static Long remove(@NonNull final String key, Object... values) {
        Long size = null;
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(OBJECT_REDIS_TEMPLATE.hasKey(key))) {
                size = OBJECT_REDIS_TEMPLATE.opsForSet().remove(key, values);
            }
        } catch (Exception e) {
            log.error("删除Redis失败!", e);
        }
        return size;
    }

    /*------------------------------- String Object 结束 -------------------------------*/

    /*------------------------------- 公共 String 开始 -------------------------------*/

    /**
     * 通过key删除
     *
     * @param key key
     * @return 删除结果
     */
    public static boolean delete(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.delete(key));
            }
        } catch (Exception e) {
            log.error("删除Redis失败!", e);
        }
        return false;
    }

    /**
     * key是否存在
     *
     * @param key key
     * @return 是否存在
     */
    public static boolean hasKey(@NonNull final String key) {
        try {
            if (StrUtil.isNotBlank(key)) {
                return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key));
            }
        } catch (Exception e) {
            log.error("查询Redis是否存在key失败!", e);
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param key  key
     * @param time 过期时间
     * @return 是否设置成功
     */
    public static boolean expire(@NonNull final String key, long time) {
        try {
            if (StrUtil.isNotBlank(key) && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key))) {
                return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS));
            }
        } catch (Exception e) {
            log.error("设置Redis过期时间失败!", e);
        }
        return false;
    }

    /*------------------------------- 公共 String 结束 -------------------------------*/

    /*------------------------------- 公共 Object 开始 -------------------------------*/

    public static Set<String> keys(@NonNull final String prefixKey) {
        try {
            return STRING_REDIS_TEMPLATE.keys(prefixKey);
        } catch (Exception e) {
            log.error("获取 Redis keys失败!", e);
        }
        return null;
    }

    /*------------------------------- 公共 Object 结束 -------------------------------*/
}
