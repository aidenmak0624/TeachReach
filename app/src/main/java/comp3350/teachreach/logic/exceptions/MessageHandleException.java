package comp3350.teachreach.logic.exceptions;

import comp3350.teachreach.logic.communication.MessageHandler;

public class MessageHandleException extends Exception {
    public MessageHandleException (String message, Throwable cause){ super(message);}
}
