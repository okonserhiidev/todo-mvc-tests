package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.hidden;

@Feature("Clear completed")
public class ClearCompletedTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Bulk clear")
    @Description("'Clear completed' wipes only completed todos and hides the button afterwards.")
    public void clearCompleted_removesCompletedOnly_hidesButtonWhenNone() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_3);
        page.list().itemByText(TodoTexts.SIMPLE_TEXT_2).toggle();
        page.list().itemByText(TodoTexts.SIMPLE_TEXT_3).toggle();

        page.footer().clearCompleted();

        page.list().items().shouldHave(exactTexts(TodoTexts.SIMPLE_TEXT_1));
        page.footer().clearCompletedButton().shouldBe(hidden);
    }
}
