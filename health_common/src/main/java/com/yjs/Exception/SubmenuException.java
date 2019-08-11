package com.yjs.Exception;

//删除菜单或者修改菜单等级时，判断该菜单是否有子菜单，如果有，抛出的异常
public class SubmenuException extends RuntimeException{
    public SubmenuException() {
    }

    public SubmenuException(String message) {
        super(message);
    }

    public SubmenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubmenuException(Throwable cause) {
        super(cause);
    }

    public SubmenuException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
