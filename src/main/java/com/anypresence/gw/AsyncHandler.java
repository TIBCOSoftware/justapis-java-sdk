package com.anypresence.gw;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

class AsyncHandler {
	private static ListeningExecutorService service = MoreExecutors
			.listeningDecorator(Executors.newFixedThreadPool(2));

	public static ListeningExecutorService getService() {
		return service;
	}
	
	public static void shutdownServices() {
	    getService().shutdown();
	}

	public static <V> void handleCallbacks(ListenableFuture<V> future,
			IAPFutureCallback<V> futureCallback,
			IAPFutureCallback<V>... optionalCallbacks) {
		if (futureCallback != null) {
			Futures.addCallback(future, futureCallback);

			for (IAPFutureCallback<V> optionalCallback : optionalCallbacks) {
				Futures.addCallback(future, optionalCallback);
			}
		}
	}
}
