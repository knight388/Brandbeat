package com.mgrmobi.brandbeat.exeption;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BaseExeption extends Throwable {
    private final int CODE;

    public BaseExeption() {
        CODE = -1;
    }

    public BaseExeption(String detailMessage) {
        super(detailMessage);
        CODE = -1;
    }

    public BaseExeption(String detailMessage, int code) {
        super(detailMessage);
        CODE = code;
    }

    public BaseExeption(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        CODE = -1;
    }

    public BaseExeption(Throwable cause) {
        super(cause);
        CODE = -1;
    }

    public int getCode() {
        return CODE;
    }
}
