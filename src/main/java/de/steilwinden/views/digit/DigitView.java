package de.steilwinden.views.digit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import de.steilwinden.views.MainLayout;

@PageTitle("Digit")
@Route(value = "digit", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@CssImport("./views/digit/digit-view.css")
public class DigitView extends VerticalLayout {

    private final DigitPresenter presenter;
    final SignaturePad signaturePad = new SignaturePad(300,300);

    public DigitView(final DigitPresenter presenter) {
        this.presenter = presenter;
        initView();
        presenter.init(this);
    }

    public void initView() {
        addClassName("digit-view");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        buildSignaturePad(mainLayout);
        buildButtonLayout(mainLayout);
        add(mainLayout);
    }

    private void buildSignaturePad(VerticalLayout mainLayout) {
        signaturePad.getElement().getClassList().add("framing");
        signaturePad.setStrokeStyle("#33AFFF");
        signaturePad.setLineWidth(2);
        mainLayout.add(signaturePad);
    }

    private void buildButtonLayout(VerticalLayout mainLayout) {
        Button saveButton = new Button("Guess Digit");
        saveButton.addClickListener(e -> presenter.guessDigit());
        Button clearButton = new Button("Clear");
        clearButton.addClickListener(e -> signaturePad.onClear());
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, clearButton);
        mainLayout.add(buttonLayout);
    }

}
