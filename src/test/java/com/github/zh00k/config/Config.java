package com.github.zh00k.config;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final  String WEBSHOP_URL             = "https://demowebshop.tricentis.com";
    private static final String KAITEN_CARDS            = "https://qafeelsgood.kaiten.ru/space/841545/lists/card/";
    public static final  String KAITEN_JAVA_BASICS_URL  = KAITEN_CARDS + "71053253";
    public static final  String KAITEN_ARCHITECTURE_URL = KAITEN_CARDS + "71053254";
    public static final  String KAITEN_JUNIT_URL        = KAITEN_CARDS + "71053255";
    public static final  String KAITEN_ALLURE_URL       = KAITEN_CARDS + "71053252";

    private static final WebDriverConfig CONFIG = ConfigFactory.create(WebDriverConfig.class, System.getProperties());

    private static Map<String, Object> getSelenoidOptions() {
        Map<String, Object> selenoidOptions = new HashMap<>();

        selenoidOptions.put("name", CONFIG.sessionName());
        selenoidOptions.put("sessionTimeout", CONFIG.sessionTimeout());
        selenoidOptions.put("screenResolution", CONFIG.screenResolution());
        selenoidOptions.put("labels", Map.of("manual", "true"));
        selenoidOptions.put("enableVNC", CONFIG.enableVNC());
        selenoidOptions.put("enableVideo", CONFIG.enableVideo());
        selenoidOptions.put("enableHAR", CONFIG.enableHAR());
        selenoidOptions.put("enableLog", CONFIG.enableLog());

        return selenoidOptions;
    }

    private static void setRemoteChromeOptions() {
        Configuration.remote = "https://"
                + CONFIG.selenoidUser() + ":"
                + CONFIG.selenoidPassword() + "@"
                + CONFIG.selenoidUrl() + "/wd/hub";

        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", CONFIG.browserVersion());
        options.setCapability("selenoid:options", getSelenoidOptions());

        Configuration.browserCapabilities = options;
    }

    public static void setBrowserOptions() {
        Configuration.browser = CONFIG.browser();
        Configuration.browserVersion = CONFIG.browserVersion();
        Configuration.browserSize = CONFIG.screenResolution();

        if ("remote".equals(CONFIG.run())) {
            setRemoteChromeOptions();
        }
    }
}
