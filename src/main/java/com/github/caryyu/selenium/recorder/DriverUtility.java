package com.github.caryyu.selenium.recorder;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverUtility {
    public static WebDriver wdriver = null;

    public static WebDriver StartDriver() {
        String driverPath = System.getProperty("driver.path");
        String driverMode = System.getProperty("driver.mode");
        String driverAddress = System.getProperty("driver.address");
        String driverArguments = System.getProperty("driver.arguments");

        if (StringUtils.equalsIgnoreCase(driverMode, "local")) {
            System.setProperty("webdriver.chrome.driver", driverPath);
            wdriver = new ChromeDriver(getChromeOptionsByString(driverArguments));
        } else if (StringUtils.equalsIgnoreCase(driverMode, "remote")) {
            URL url = null;
            try {
                url = new URL(driverAddress);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            wdriver = new RemoteWebDriver(url, getCapabilitiesByString(driverArguments));
        } else {
            throw new UnsupportedOperationException("Driver mode value is range of [local,remote]");
        }

        wdriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS); // 超时等待时间20s
        return wdriver;
    }

    private static ChromeOptions getChromeOptionsByString(String argumentsString) {
        ChromeOptions chromeOptions = new ChromeOptions();

        if(StringUtils.isNotEmpty(argumentsString)){
            String[] chromeArguments = argumentsString.split(" ");
            chromeOptions.addArguments(chromeArguments);
        }

        return chromeOptions;
    }

    private static DesiredCapabilities getCapabilitiesByString(String argumentsString) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chromeOptions", getChromeOptionsByString(argumentsString));
        return capabilities;
    }

    /**
     * 滑动到页面最下面
     */
    public static void scrollToBottomOfDocument() {
        if (wdriver instanceof JavascriptExecutor) {
            JavascriptExecutor d = (JavascriptExecutor) wdriver;
            d.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        }
    }

    /**
     * 把当前元素ele移动到窗口的顶部
     */
    public static void scrollElementToTop(WebElement ele) {
        if (wdriver instanceof JavascriptExecutor) {
            JavascriptExecutor d = (JavascriptExecutor) wdriver;
            d.executeScript("arguments[0].scrollIntoView(true);",ele);
        }
    }

    /**
     * 延时多长时间滑动到页面最下面
     * @param delay 延时(秒/单位)
     */
    public static void scrollToBottomOfDocument(long delay) {
        try {
            Thread.sleep(delay);
            scrollToBottomOfDocument();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void quitDriver() {
        if (wdriver != null) {
            wdriver.manage().deleteAllCookies();
            wdriver.quit();
            wdriver = null;
        }
    }
}
