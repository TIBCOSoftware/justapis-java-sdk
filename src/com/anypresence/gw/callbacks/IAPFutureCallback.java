package com.anypresence.gw.callbacks;

import com.google.common.util.concurrent.FutureCallback;

public interface IAPFutureCallback<T> extends FutureCallback<T> {
	void finished(T object, Throwable ex);
}
