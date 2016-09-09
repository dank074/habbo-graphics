package ch.habbo.graphics.tools.swf;

import com.jpexs.decompiler.flash.AbortRetryIgnoreHandler;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleAbortRetryIgnoreHandler implements AbortRetryIgnoreHandler {

    int errorCount = 0;

    int errorMode;

    int retryCount;

    public ConsoleAbortRetryIgnoreHandler(int errorMode, int retryCount) {
        this.errorMode = errorMode;
        this.retryCount = retryCount;
    }

    @Override
    public int handle(Throwable thrown) {
        System.out.println(thrown.getMessage());
        return AbortRetryIgnoreHandler.IGNORE;
        
    }

    @Override
    public AbortRetryIgnoreHandler getNewInstance() {
        return new ConsoleAbortRetryIgnoreHandler(errorMode, retryCount);
    }
}
