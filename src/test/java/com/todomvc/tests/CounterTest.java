package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.Condition.exactText;

@Feature("Counter")
public class CounterTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Pluralization")
    @Description("Counter text correctly pluralises 'item'/'items' for 1, multiple and zero active todos.")
    public void counter_showsCorrectPluralization() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.footer().counter().shouldHave(exactText("1 item left!"));

        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.footer().counter().shouldHave(exactText("2 items left!"));

        page.list().toggleAll();
        page.footer().counter().shouldHave(exactText("0 items left!"));
    }
}
