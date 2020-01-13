package com.fuli.cloud.commons.utils;

public class ExcelException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 5244818718206274668L;

    /**
     * Constructs a new exception with the specified detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     */
    public ExcelException(String message) {
        super(message);
    }
}
