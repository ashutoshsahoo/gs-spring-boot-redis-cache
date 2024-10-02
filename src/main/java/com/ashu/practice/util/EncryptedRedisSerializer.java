package com.ashu.practice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

@Slf4j
public class EncryptedRedisSerializer<T> implements RedisSerializer<T> {

    private static final byte[] EMPTY_ARRAY = new byte[0];
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public EncryptedRedisSerializer(final ObjectMapper objectMapper, Class<T> type) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        Assert.notNull(type, "Java type must not be null");
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return EMPTY_ARRAY;
        }
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            return AESUtil.encrypt(jsonValue).getBytes();
        } catch (Exception e) {
            throw new SerializationException("Error during serialization", e);
        }
    }

    @Override
    public T deserialize(byte[] data) throws SerializationException {

        if (data == null || data.length == 0) {
            return null;
        }

        try {
            String decryptedValue = AESUtil.decrypt(new String(data));
            return objectMapper.readValue(decryptedValue, type);
        } catch (Exception e) {
            throw new SerializationException("Error during deserialization", e);
        }
    }
}

