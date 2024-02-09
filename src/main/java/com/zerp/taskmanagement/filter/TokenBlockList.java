package com.zerp.taskmanagement.filter;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
public class TokenBlockList {

    private Cache<String, Boolean> invalidatedToken;

    TokenBlockList() {
        invalidatedToken = CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build();
    }

    public void invalidateToken(String token) {
        invalidatedToken.put(token, true);
    }

    public boolean isBlackListed(String token) {
        return invalidatedToken.getIfPresent(token)!=null ;
    }


}
