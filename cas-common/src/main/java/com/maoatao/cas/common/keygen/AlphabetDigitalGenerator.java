package com.maoatao.cas.common.keygen;

import cn.hutool.core.util.RandomUtil;
import org.springframework.util.Assert;

/**
 * 数字+大小写字母 生成器
 *
 * @author MaoAtao
 * @date 2023-04-03 11:14:13
 */
public class AlphabetDigitalGenerator implements CasStringKeyGenerator {

    private static final String BASE_CHAR_UPPER = RandomUtil.BASE_CHAR.toUpperCase();

    private static final int DEFAULT_VALUE_LENGTH = 6;

    private final int length;

    public AlphabetDigitalGenerator() {
        this(DEFAULT_VALUE_LENGTH);
    }

    public AlphabetDigitalGenerator(int length) {
        Assert.isTrue(length >= DEFAULT_VALUE_LENGTH, "长度不能小于" + DEFAULT_VALUE_LENGTH);
        this.length = length;
    }

    @Override
    public String generate() {
        return RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER.concat(BASE_CHAR_UPPER), this.length);
    }
}
