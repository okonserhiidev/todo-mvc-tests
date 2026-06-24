package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.CollectionCondition.noneMatch;
import static com.codeborne.selenide.Condition.exactText;

@Feature("Toggle all")
public class ToggleAllTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Bulk toggle round-trip")
    @Description("Toggle-all marks every todo completed; a second click flips them all back to active.")
    public void toggleAll_marksAllCompleted_thenAllActive() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_3);

        page.list().toggleAll();

        page.list().items()
                .shouldHave(allMatch("all completed",
                        el -> el.getAttribute("class").contains("completed")));
        page.footer().counter().shouldHave(exactText("0 items left!"));

        page.list().toggleAll();

        page.list().items()
                .shouldHave(noneMatch("none completed",
                        el -> el.getAttribute("class").contains("completed")));
        page.footer().counter().shouldHave(exactText("3 items left!"));
    }
}
