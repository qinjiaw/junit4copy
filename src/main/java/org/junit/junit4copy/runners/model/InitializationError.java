package org.junit.junit4copy.runners.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by qinjw on 2016/7/14.
 */
public class InitializationError extends Exception {
    private static final long serialVersionID = 1L;

    private final List<Throwable> fErrors;
    public InitializationError(List<Throwable> errors) {
        this.fErrors = errors;
    }

    public InitializationError(Throwable error) {
        this(Arrays.asList(error));
    }

    public InitializationError(String string) {
        this(new Exception(string));
    }

    public List<Throwable> getCauses() {
        return fErrors;
    }


}
