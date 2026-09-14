package com.jagex;

import java.util.Map;

import static java.util.Map.entry;

public final class AppletParameters {

    public static Map<String, String> createDefault() {
        return Map.ofEntries(
            entry("cabbase", "g.cab"),
            entry("java_arguments", "-Xmx256m -Dsun.java2d.noddraw=true"),
            entry("colourid", "0"),
            entry("worldid", "1"),
            entry("lobbyid", "1000"),
            entry("lobbyaddress", "127.0.0.1"),
            entry("demoid", "0"),
            entry("demoaddress", ""),
            entry("modewhere", "1"),
            entry("modewhat", "0"),
            entry("lang", "0"),
            entry("objecttag", "0"),
            entry("js", "1"),
            entry("game", "0"),
            entry("affid", "0"),
            entry("advert", "1"),
            entry("settings", "wwGlrZHF5gJcZl7tf7KSRh0MZLhiU0gI0xDX6DwZ-Qk"),
            entry("country", "0"),
            entry("haveie6", "0"),
            entry("havefirefox", "1"),
            entry("cookieprefix", ""),
            entry("cookiehost", "127.0.0.1"),
            entry("cachesubdirid", "0"),
            entry("crashurl", ""),
            entry("unsignedurl", ""),
            entry("sitesettings_member", "1"),
            entry("frombilling", "false"),
            entry("sskey", ""),
            entry("force64mb", "false"),
            entry("worldflags", "8")
        );
    }

    private AppletParameters() {
        /* empty */
    }
}
