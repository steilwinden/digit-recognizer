package de.steilwinden.views.digit;

import com.vaadin.flow.dom.DomEvent;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class SignaturePad extends Canvas {

    private boolean drawing;
    private double x;
    private double y;
    private static final String LEFT_MOUSE_BUTTON_CODE = "1.0";
    private final int width;
    private final int height;

    public SignaturePad(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;

        // https://developer.mozilla.org/en-US/docs/Web/API/Element/mousemove_event
        getElement().addEventListener("mousedown", this::onMouseDown)
                .addEventData("event.offsetX")
                .addEventData("event.offsetY");

        getElement().addEventListener("mousemove", this::onMouseMove)
                .setFilter("event.buttons == '" + LEFT_MOUSE_BUTTON_CODE + "'")
                .addEventData("event.offsetX")
                .addEventData("event.offsetY");

        getElement().addEventListener("mouseup", this::onMouseUp)
                .addEventData("event.offsetX")
                .addEventData("event.offsetY");

        // https://www.w3schools.com/jsref/dom_obj_all.asp
        getElement().addEventListener("touchstart", this::onTouchStart)
                .addEventData("event.preventDefault()")
                .addEventData("event.touches[0].pageX")
                .addEventData("event.touches[0].pageY")
                .addEventData("element.offsetLeft")
                .addEventData("element.offsetTop");

        // https://developer.mozilla.org/en-US/docs/Web/API/Element/touchmove_event
        getElement().addEventListener("touchmove", this::onTouchMove)
                .addEventData("event.preventDefault()")
                .addEventData("event.touches[0].pageX")
                .addEventData("event.touches[0].pageY")
                .addEventData("element.offsetLeft")
                .addEventData("element.offsetTop");

        getElement().addEventListener("touchend", this::onTouchEnd)
                .addEventData("event.preventDefault()");
    }

    private void onMouseDown(DomEvent event) {
        // https://developer.mozilla.org/en-US/docs/Web/API/MouseEvent
        JsonObject eventData = event.getEventData();
        x = eventData.getNumber("event.offsetX");
        y = eventData.getNumber("event.offsetY");
        drawing = true;
    }

    private void onMouseMove(DomEvent event) {
        JsonObject eventData = event.getEventData();
        double offsetX = eventData.getNumber("event.offsetX");
        double offsetY = eventData.getNumber("event.offsetY");

        if (drawing) {
            drawLine(x, y, offsetX, offsetY);
            x = offsetX;
            y = offsetY;
        }
    }

    private void onMouseUp(DomEvent event) {
        JsonObject eventData = event.getEventData();
        double offsetX = eventData.getNumber("event.offsetX");
        double offsetY = eventData.getNumber("event.offsetY");

        if (drawing) {
            drawLine(x, y, offsetX, offsetY);
            drawing = false;
        }
    }

    private void onTouchStart(DomEvent event) {
        // https://developer.mozilla.org/en-US/docs/Web/API/Touch
        JsonObject eventData = event.getEventData();
        x = eventData.getNumber("event.touches[0].pageX") - eventData.getNumber("element.offsetLeft");
        y = eventData.getNumber("event.touches[0].pageY") - eventData.getNumber("element.offsetTop");
        drawing = true;
    }

    private void onTouchMove(DomEvent event) {
        JsonObject eventData = event.getEventData();
        double pageX = eventData.getNumber("event.touches[0].pageX") - eventData.getNumber("element.offsetLeft");
        double pageY = eventData.getNumber("event.touches[0].pageY") - eventData.getNumber("element.offsetTop");

        if (drawing) {
            drawLine(x, y, pageX, pageY);
            x = pageX;
            y = pageY;
        }
    }

    private void onTouchEnd(DomEvent event) {
        drawing = false;
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        beginPath();
        moveTo(x1, y1);
        lineTo(x2, y2);
        stroke();
        closePath();
    }

    public void onClear() {
        clearRect(0, 0, width, height);
    }

    public void onSave(Consumer<BufferedImage> consumer) {
        saveAsFile(e -> resultHandler(e, consumer));
    }

    private void resultHandler(JsonValue json, Consumer<BufferedImage> consumer) {
        String base64Image = json.asString().split(",")[1];
        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            consumer.accept(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
