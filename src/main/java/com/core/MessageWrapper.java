package com.core;

import java.io.Serializable;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class MessageWrapper implements Serializable {
    private static final long serialVersionUID = -2476263092040373362L;
    private String message;

    public static MessageWrapper wrap(String message){
        return new MessageWrapper(message);
    }

    private MessageWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
