package com.swarmer.shared.exceptions;

/**
 * Created by Matt on 03/16/2017.
 */
public class PlayerNotFoundException extends Exception {

    public PlayerNotFoundException() {

    }

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
