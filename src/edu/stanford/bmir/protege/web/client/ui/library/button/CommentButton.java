package edu.stanford.bmir.protege.web.client.ui.library.button;

import com.google.gwt.user.client.ui.Button;
import com.gwtext.client.widgets.form.Label;

public class CommentButton extends Button {

    private static final String TEXT = "";

    public static final String WEB_PROTEGE_COMMENT_BUTTON_STYLE_NAME = "web-protege-comment-button";

    public CommentButton() {
        super(TEXT);
        Label deleteLabel = new Label(TEXT);
        deleteLabel.addStyleName(WEB_PROTEGE_COMMENT_BUTTON_STYLE_NAME);
//        add(deleteLabel);
        setTitle("Comment");
        addStyleName("web-protege-dialog-button-compact");
        addStyleName("web-protege-dialog-button-comment");
    }
}
