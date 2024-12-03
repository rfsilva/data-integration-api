package br.com.rodrigo.dataintegration.message;

import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j2
public class MessageManager {

    private static final ResourceBundle resourceMessage = ResourceBundle.getBundle("message");

    private MessageManager() {
        // construtor vazio
    }

    public static String getMessage(String key, Object... args) {
        String text;

        try {
            text = resourceMessage.getString(key);
        } catch (MissingResourceException ex) {
            log.debug(ex.getMessage(), ex);
            text = key;
        }

        if ((args != null) && (args.length > 0)) {
            text = MessageFormat.format(text, args);
        }

        return text;
    }
}
