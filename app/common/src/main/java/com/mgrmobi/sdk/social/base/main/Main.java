package com.mgrmobi.sdk.social.base.main;

import com.mgrmobi.sdk.social.base.GenericSocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.base.logger.SocialLogger;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world");
        final SocialNetwork socialNetwork = new TestSocialNetwork();
        ((GenericSocialNetwork)socialNetwork).setLogger(new SocialLogger() {
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        });
        socialNetwork.prepare();
        System.out.println("social type: " + socialNetwork.getSocialType());
    }

    static class TestSocialNetwork extends GenericSocialNetwork {

        @Override
        public void prepare() {
            log("prepare");
        }

        @Override
        public SocialType getSocialType() {
            return SocialType.FACEBOOK;
        }

        @Override
        public String getAccessToken() {
            return null;
        }

        @Override
        public String getUserId() {
            return null;
        }

        @Override
        public void profile() {

        }
    }
}
