package com.github.caryyu.selenium.recorder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Scanner;

/**
 * Created by cary on 8/15/17.
 */
public class ActionRecorder {
    private WebDriver webDriver;

    public ActionRecorder(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void start() {
        doRootRound();
    }

    public void doRootRound() {
        System.out.println("请选择下列你要执行的动作,");
        System.out.println("1、打开一个网址;");
        System.out.println("2、通过XPath抓取节点并进行后续操作;");
        System.out.println("3、退出;");

        switch (getCommand(Integer.class,"输入: ")){
            case 1:
                doOption1();
                break;
            case 2:
                doOption2();
                break;
            case 3:
                break;
        }
    }

    public void doOption1() {
        String url = getCommand("请输入你要开始的网址：");
        try {
            webDriver.get(url);
            doRootRound();
        } catch (Exception e){
            e.printStackTrace();
            doOption1();
        }
    }

    public void doOption2() {
        try {
            WebElement element = webDriver.findElement(By.xpath(getCommand("请输入一个XPATH表达式: ")));

            System.out.println("请选择下列你要执行的动作,");
            System.out.println("1、Click;");
            System.out.println("2、输入值");
            System.out.println("3、退出");

            switch (getCommand(Integer.class,"输入: ")){
                case 1:
                    element.click();
                    break;
                case 2:
                    element.sendKeys(getCommand("输入值: "));
                    break;
                case 3:
                    break;
            }
            doRootRound();
        } catch (Exception e){
            e.printStackTrace();
            doOption2();
        }
    }

    private String getCommand(String message) {
        return getCommand(String.class,message);
    }

    private <T> T getCommand(Class<T> t,String message) {
        if(message != null){
            System.out.print(message);
        }
        Scanner scanner = new Scanner(System.in);
        if(scanner.hasNext()) {
            if (Integer.class.isAssignableFrom(t)){
                return (T)new Integer(scanner.nextInt());
            }else if(String.class.isAssignableFrom(t)) {
                return (T)scanner.next();
            }else{
                return (T)scanner.next();
            }
        }
        return getCommand(t,message);
    }

    public static void main(String[] args) {
        System.setProperty("driver.path","/Users/cary/Desktop/chromedriver");
        System.setProperty("driver.mode","local");

        WebDriver webDriver = DriverUtility.StartDriver();
        ActionRecorder actionRecorder = new ActionRecorder(webDriver);
        actionRecorder.start();
        DriverUtility.quitDriver();
    }
}
