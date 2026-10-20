package com.github.zh00k.util;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.net.URI;

public class AttachmentManager {
    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    public static byte[] screenshot() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public static String video(String sessionId) {
        return "<html><body><video width='100%' height='100%' controls autoplay muted>"
                + "<source src='" + videoUrl(sessionId) + "' type='video/mp4'>"
                + "</video></body></html>";
    }

    private static String videoUrl(String sessionId) {
        URI remote = URI.create(Configuration.remote);
        String host = remote.getScheme() + "://" + remote.getHost()
                + (remote.getPort() == -1 ? "" : ":" + remote.getPort());
        return host + "/video/" + sessionId + ".mp4";
    }
}
