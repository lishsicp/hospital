package com.yaroslav.lobur.tags;

import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ResourceBundle;

public class CheckBundleKeyTag extends SimpleTagSupport {

    private static final Logger logger = LoggerFactory.getLogger(CheckBundleKeyTag.class);

    String key;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void doTag() {
        if (key == null) return;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("message");
        logger.trace("Contains key {}", resourceBundle.containsKey(key));
        getJspContext().setAttribute("contains", resourceBundle.containsKey(key));
    }
}
