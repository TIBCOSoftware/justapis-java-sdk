package callbacks;

public abstract class APCallback<T> implements IAPFutureCallback<T> {
    public void onFailure(final Throwable ex) {
        finished(null, ex);
    }

    public void onSuccess(final T object) {
        finished(object, null);
    }

    public abstract void finished(T object, Throwable ex);
    
}