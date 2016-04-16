package com.tbg.simplestvallet.app.manager.authentication;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wws2003 on 10/2/15.
 */
public class SVCredential {

    public static final String SERVICE_NAME_GOOGLE_DRIVE = "google_drive";

    //Map from service name -> [account name in this service, access token, and may be more]
    private Map<String, String[]> mServiceMap;

    private SVCredential() {
        mServiceMap = new HashMap<>();
    }

    public String getServiceAccessToken(String serviceName) {
        String[] serviceInfo = mServiceMap.get(serviceName);
        if(serviceInfo != null) {
            return serviceInfo[1];
        }
        return null;
    }

    public String getServiceAccountName(String serviceName) {
        String[] serviceInfo = mServiceMap.get(serviceName);
        if(serviceInfo != null) {
            return serviceInfo[0];
        }
        return null;
    }

    public void getAllServiceNames(List<String> serviceNames) {
        serviceNames.clear();
        for(String serviceName : mServiceMap.keySet()) {
            serviceNames.add(serviceName);
        }
    }

    public static class Builder {
        private SVCredential mCredential = new SVCredential();

        public void configureService(String serviceName, String accountName, String accessToken) {
            String[] serviceInfo = mCredential.mServiceMap.get(serviceName);
            if(serviceInfo == null) {
                serviceInfo = new String[2];
            }
            serviceInfo[0] = accountName;
            serviceInfo[1] = accessToken;

            //Is this really necessary. A very basic JAVA question
            mCredential.mServiceMap.put(serviceName, serviceInfo);
        }

        public SVCredential build() {
            return mCredential;
        }
    }

}
