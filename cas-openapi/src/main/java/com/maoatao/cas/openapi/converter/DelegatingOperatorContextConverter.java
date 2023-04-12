package com.maoatao.cas.openapi.converter;

import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import com.maoatao.synapse.lang.exception.SynaException;
import com.maoatao.synapse.lang.util.SynaAssert;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 委派上下文
 *
 * @author MaoAtao
 * @date 2023-03-26 12:50:37
 */
@Slf4j
public class DelegatingOperatorContextConverter implements ContextConverter {

    private List<ContextConverter> converters;

    public DelegatingOperatorContextConverter(ContextConverter... converters) {
        this(List.of(converters));
    }

    public DelegatingOperatorContextConverter(List<ContextConverter> converters) {
        setConverters(converters);
    }

    @Override
    public DaedalusOperatorContext convert(String token) throws SynaException {
        DaedalusOperatorContext result;
        int currentPosition = 0;
        int size = this.converters.size();
        for (ContextConverter converter : getConverters()) {
            log.debug("Converting token with {} ({}/{})", converter.getClass().getSimpleName(), ++currentPosition, size);
            result = converter.convert(token);
            if (result != null) {
                return result;
            }
        }
        log.warn("No converter is available for the token {}", token);
        return null;
    }

    public List<ContextConverter> getConverters() {
        return converters;
    }

    public void setConverters(List<ContextConverter> converters) {
        SynaAssert.notEmpty(converters, "converters list cannot contain null values");
        this.converters = converters;
    }
}
