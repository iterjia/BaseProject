package com.hy.modules.auth.entity;

import com.hy.core.secure.entity.TokenStub;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用于请求API
     */
    private String accessToken;

    /**
     * 用于刷新token
     */
    private String refreshToken;

    /**
     * 过期时间
     */
    private long expiresIn;

    /**
     * 鉴权中包含的附加信息
     */
    private TokenStub tokenStub;

}
