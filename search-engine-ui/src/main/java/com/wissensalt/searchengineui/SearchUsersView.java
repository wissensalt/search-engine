package com.wissensalt.searchengineui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@PageTitle("Search Users")
@Route(value = "search", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class SearchUsersView extends Div {

    private Grid<UserDTO> grid;
    private TextField tfSearch;
    private NumberField tfLimit;
    private NumberField tfPage;


    public SearchUsersView() {
        grid = new Grid<>(UserDTO.class, false);
        setupSearchForm();
    }

    private void setupSearchForm() {
        tfSearch =  new TextField("Search Something");
        tfSearch.setValueChangeMode(ValueChangeMode.LAZY);
        tfSearch.setValueChangeTimeout(500);
        tfLimit = new NumberField("Limit");
        tfLimit.setValue(10.0);
        tfPage = new NumberField("Page");
        tfPage.setValue(0.0);
        tfSearch.addFocusListener(e -> tfSearch.setValue(""));
        tfSearch.addKeyDownListener(Key.ENTER, e -> search());
        tfLimit.addKeyUpListener(e -> search());
        tfPage.addKeyUpListener(e -> search());

        HorizontalLayout searchForm = new HorizontalLayout(tfSearch, tfLimit, tfPage);
        VerticalLayout layout = new VerticalLayout(searchForm, grid);
        add(layout);
    }

    private void search() {
        if (StringUtils.isBlank(tfSearch.getValue())) {
            return;
        }

        String limit = tfLimit.getValue() <= 0 ? "10" : String.valueOf(Math.round(tfLimit.getValue()));
        String page = tfPage.getValue() <= 0 ? "0" : String.valueOf(Math.round(tfPage.getValue()));
        if (StringUtils.isBlank(tfSearch.getValue())) {
            limit = "10";
            page = "0";
        }
        List<UserDTO> userList = BackendOutbound.getUsers(tfSearch.getValue(), limit, page);
        grid.removeAllColumns();
        grid.addColumn(UserDTO::getFirstName).setHeader("First Name");
        grid.addColumn(UserDTO::getMiddleName).setHeader("Middle Name");
        grid.addColumn(UserDTO::getLastName).setHeader("Last Name");

        grid.setItems(userList);
        grid.setVisible(true);
        grid.getDataProvider().refreshAll();
    }
}
