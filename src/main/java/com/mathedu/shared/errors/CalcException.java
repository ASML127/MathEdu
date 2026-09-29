package com.mathedu.shared.errors;

/** Excepcion de dominio: siempre lleva un {@link ErrorCode} + campo opcional + pista. */
public class CalcException extends Exception {

    private final ErrorCode code;
    private final String field;

    public CalcException(ErrorCode code, String message) {
        super(message);
        this.code = code;
        this.field = null;
    }

    public CalcException(ErrorCode code, String field, String message) {
        super(message);
        this.code = code;
        this.field = field;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getField() {
        return field;
    }
}
