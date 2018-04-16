package com.mgrmobi.sdk.social.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Valentin S. Bolkonsky.
 */
public class SocialNetworkManager {

    private static final Map<SocialType, SocialNetwork> networks = new ConcurrentHashMap<>();

    private SocialNetworkManager() {

    }

    public static void registrationNetwork(final SocialNetwork network) {
        networks.remove(network.getSocialType());
        networks.put(network.getSocialType(), network);
    }

    public static SocialNetwork getNetwork(final SocialType socialType) {
        return networks.get(socialType);
    }

    public static boolean isRegistered(final SocialType socialType) {
        return networks.containsKey(socialType);
    }


}
