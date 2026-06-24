package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.not;

@Feature("Toggle single Todo")
public class ToggleTodoTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Smoke")
    @Description("Clicking the checkbox marks the todo completed and removes it from the active counter.")
    public void toggle_activeTodo_marksCompleted() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        page.list().itemAt(0).toggle();

        page.list().itemAt(0).self().shouldHave(cssClass("completed"));
        page.footer().counter().shouldHave(exactText("0 items left!"));
    }

    @Test
    @Story("Toggle off")
    @Description("Toggling a completed todo brings it back to the active state and the counter rises again.")
    public void toggle_completedTodo_marksActive() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.list().itemAt(0).toggle();
        page.list().itemAt(0).self().shouldHave(cssClass("completed"));

        page.list().itemAt(0).toggle();

        page.list().itemAt(0).self().shouldHave(not(cssClass("completed")));
        page.footer().counter().shouldHave(exactText("1 item left!"));
    }
}
