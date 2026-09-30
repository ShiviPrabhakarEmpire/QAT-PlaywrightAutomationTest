package com.qat.playwright;

public class TestDevice {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("com.microsoft.playwright.Playwright");
            System.out.println("Methods:");
            for (java.lang.reflect.Method m : clazz.getMethods()) {
                System.out.println(m.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
