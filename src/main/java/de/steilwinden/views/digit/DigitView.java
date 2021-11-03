package de.steilwinden.views.digit;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import de.steilwinden.views.MainLayout;

@PageTitle("Digit")
@Route(value = "digit", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DigitView extends VerticalLayout {

    public DigitView() {
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//        getStyle().set("text-align", "center");
    }

}
