package com.swarmer.shared.exceptions;

/**
 * Created by Matt on 03/29/2017.
 */
public class GameClientNotInstantiatedException extends Throwable {

    public GameClientNotInstantiatedException() {
    }

    public GameClientNotInstantiatedException(String message) {
        super(message);
    }
}
