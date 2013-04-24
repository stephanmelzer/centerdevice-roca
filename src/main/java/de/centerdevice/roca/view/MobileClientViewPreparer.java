package de.centerdevice.roca.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;

public class MobileClientViewPreparer implements ViewPreparer {

    HashMap<String, String> userAgentAppStores;

    public MobileClientViewPreparer() {
        userAgentAppStores = new HashMap<>();
        userAgentAppStores.put("Android", "https://play.google.com/store/apps");
        userAgentAppStores.put("iPhone", "http://itunes.com/apps/CenterDevice");
        userAgentAppStores.put("iPad", "http://itunes.com/apps/CenterDevice");
        userAgentAppStores.put("Windows Phone", "http://www.windowsphone.com/en-us/store/overview");

    }

    @Override
    public void execute(Request tilesContext, AttributeContext attributeContext) {
        boolean isMobileClient = false;
        String appStoreLink = "";
        Map<String, String[]> headerValues = tilesContext.getHeaderValues();
        String requestUserAgent = headerValues.get("user-agent")[0];

        for (String userAgent : userAgentAppStores.keySet()) {
            if (requestUserAgent.toLowerCase().contains(userAgent.toLowerCase())) {
                appStoreLink = userAgentAppStores.get(userAgent);
                isMobileClient = true;
            }
        }

        tilesContext.getContext("request").put("isMobileClient", isMobileClient);
        tilesContext.getContext("request").put("appStoreLink", appStoreLink);

    }
}
