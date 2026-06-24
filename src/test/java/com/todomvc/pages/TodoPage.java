package com.todomvc.pages;

import com.todomvc.pages.components.FooterComponent;
import com.todomvc.pages.components.HeaderComponent;
import com.todomvc.pages.components.TodoListComponent;

public class TodoPage {

    private final HeaderComponent header = new HeaderComponent();
    private final TodoListComponent list = new TodoListComponent();
    private final FooterComponent footer = new FooterComponent();

    public HeaderComponent header() {
        return header;
    }

    public TodoListComponent list() {
        return list;
    }

    public FooterComponent footer() {
        return footer;
    }
}
