package de.steilwinden.views.digit;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.steilwinden.service.ImageService;
import de.steilwinden.service.NetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class DigitPresenter {

    private final ImageService imageService;
    private final NetworkService networkService;
    private DigitView view;
    static final String PNG_PATHNAME = NetworkService.ADHOC_FOLDER + "/foto.png";

    void init(final DigitView view) {
        this.view = view;
    }

    void guessDigit() {
        view.signaturePad.onSave(this::onGuessDigit);
    }

    private void onGuessDigit(BufferedImage image) {
        image = imageService.thickenLine(image);
        image = imageService.centerImage(image);
        imageService.print(image);
        image = imageService.resizeImage(image);

        File file = new File(PNG_PATHNAME);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            ImageIO.write(image, "png", file);
            int digit = networkService.guessDigit();
            Notification.show("Result: " + digit, 3000, Notification.Position.MIDDLE);
        } catch (IOException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            log.error(e.getMessage(), e);
        }
    }
}
