package com.anypresence.gw;

import com.google.common.util.concurrent.FutureCallback;

interface IAPFutureCallback<T> extends FutureCallback<T> {
    void finished(T object, Throwable ex);
}
