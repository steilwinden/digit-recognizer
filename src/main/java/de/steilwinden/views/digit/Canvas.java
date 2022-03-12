package de.steilwinden.views.digit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonValue;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Tag("canvas")
public class Canvas extends Component {
// https://github.com/pekam/vaadin-flow-canvas/blob/master/src/main/java/org/vaadin/pekkam/CanvasRenderingContext2D.java

    public Canvas(int width, int height) {
        getElement().setAttribute("width", String.valueOf(width));
        getElement().setAttribute("height", String.valueOf(height));
    }

    public void setStrokeStyle(String strokeStyle) {
        setProperty("strokeStyle", strokeStyle);
    }

    public void setLineWidth(double lineWidth) {
        setProperty("lineWidth", lineWidth);
    }

    public void beginPath() {
        callJsFunction("beginPath");
    }

    public void moveTo(double x, double y) {
        callJsFunction("moveTo", x, y);
    }

    public void lineTo(double x, double y) {
        callJsFunction("lineTo", x, y);
    }

    public void stroke() {
        callJsFunction("stroke");
    }

    public void closePath() {
        callJsFunction("closePath");
    }

    public void clearRect(double x, double y, double width, double height) {
        callJsFunction("clearRect", x, y, width, height);
    }

    public void saveAsFile(SerializableConsumer<JsonValue> resultHandler) {
        getElement().executeJs("return this.toDataURL()").then(resultHandler);
    }

    protected void setProperty(String propertyName, Serializable value) {
        runScript(String.format("$0.getContext('2d').%s='%s'", propertyName, value));
    }

    private void runScript(String script) {
        getElement().getNode().runWhenAttached(
                ui -> ui.getInternals().getStateTree().beforeClientResponse(
                        getElement().getNode(), context -> ui.getPage().executeJs(script, getElement())));
    }

    private void callJsFunction(String methodName, Serializable... parameters) {
        getElement().callJsFunction("getContext('2d')." + methodName,
                parameters);
    }
}