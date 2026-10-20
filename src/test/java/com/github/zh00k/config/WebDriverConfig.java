package com.github.zh00k.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:config/${run}.properties"
})

@Config.LoadPolicy(Config.LoadType.MERGE)
public interface WebDriverConfig extends Config {
    @DefaultValue("chrome")
    String browser();

    @DefaultValue("154.0")
    String browserVersion();

    @DefaultValue("1920x1080")
    String screenResolution();

    String selenoidUrl();

    String selenoidUser();

    String selenoidPassword();

    @DefaultValue("false")
    boolean enableVideo();

    @DefaultValue("false")
    boolean enableVNC();

    @DefaultValue("false")
    boolean enableHAR();

    @DefaultValue("false")
    boolean enableLog();

    @DefaultValue("zh00k-session")
    String sessionName();

    @DefaultValue("15m")
    String sessionTimeout();

    @DefaultValue("local")
    String run();
}
