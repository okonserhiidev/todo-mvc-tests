package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.hidden;

@Feature("Delete Todo")
public class DeleteTodoTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Smoke")
    @Description("Clicking the destroy button removes the todo; when the list empties, main and footer hide.")
    public void destroy_removesTodoFromList() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        page.list().itemAt(0).delete();

        page.list().items().shouldBe(empty);
        page.list().self().shouldBe(hidden);
        page.footer().self().shouldBe(hidden);
    }
}
