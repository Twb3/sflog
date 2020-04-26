package com.twb3.manager;

import com.twb3.Org;
import com.twb3.oauth2.service.DeviceFlowService;
import com.twb3.oauth2.service.RefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthManager {
    private final static transient Logger logger = LoggerFactory.getLogger(AuthManager.class);

    public static void initialize(Org org) {
        DeviceFlowService deviceFlowService = new DeviceFlowService(org);
        deviceFlowService.doGet();
    }

    public static void refreshToken(Org org) {
        RefreshTokenService refreshTokenService = new RefreshTokenService(org);
        refreshTokenService.doRefresh();
    }
}
