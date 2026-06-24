package com.todomvc.tests;

import com.codeborne.selenide.SelenideElement;
import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@Feature("Create Todo")
@Story("XSS escaping")
public class XssEscapingTest extends BaseTest {

    private final TodoPage page = new TodoPage();
    private final String payload;

    public XssEscapingTest(String payload) {
        this.payload = payload;
    }

    @Parameterized.Parameters(name = "{index}: payload=[{0}]")
    public static Collection<Object[]> payloads() {
        return TodoTexts.XSS_PAYLOADS.stream()
                .map(p -> new Object[] {p})
                .collect(Collectors.toList());
    }

    @Test
    @Description("React escapes the payload into harmless text — no <script>, <img>, or <svg> nodes are injected.")
    public void addTodo_xssPayload_renderedAsPlainText() {
        page.header().addTodo(payload);

        page.list().items().shouldHave(size(1));
        SelenideElement labelEl = page.list().itemAt(0).label();
        labelEl.shouldHave(exactText(payload));

        long dangerousNodes = labelEl.$$("script, img, svg, iframe").size();
        assertThat(dangerousNodes)
                .as("React must not render any active DOM nodes for payload: %s", payload)
                .isZero();
    }
}
