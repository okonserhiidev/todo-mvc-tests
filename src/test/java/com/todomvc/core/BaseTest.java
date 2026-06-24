package com.todomvc.core;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Epic("TodoMVC UI Automation")
@Owner("aqa-team")
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Rule
    public final TestWatcher lifecycleLogger = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("▶ START   {}.{}", description.getClassName(), description.getMethodName());
        }

        @Override
        protected void succeeded(Description description) {
            log.info("✔ PASS    {}", description.getMethodName());
        }

        @Override
        protected void failed(Throwable e, Description description) {
            log.error("✖ FAIL    {} — {}", description.getMethodName(), e.toString());
        }

        @Override
        protected void finished(Description description) {
            log.debug("◼ DONE    {}", description.getMethodName());
        }
    };

    @BeforeClass
    public static void setupAllureReports() {
        SelenideLogger.addListener("allure",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(false));
    }

    @Before
    public void openApp() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
        }
        Selenide.open("");
    }
}
