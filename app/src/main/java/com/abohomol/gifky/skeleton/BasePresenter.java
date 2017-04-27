package com.abohomol.gifky.skeleton;

/**
 * Base class that implements the {@link Presenter} interface and provides a base implementation
 * for {@link #attachView(View)} and {@link #detachView()}.
 * It also handles keeping a reference to the view that can be accessed from the
 * children classes by calling {@link #getView()}.
 */
public abstract class BasePresenter<T extends View> implements Presenter<T> {

    private T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public T getView() {
        return view;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }

    private static class ViewNotAttachedException extends RuntimeException {
        ViewNotAttachedException() {
            super("Please call Presenter.attachView(View) before" +
                    " requesting data from the Presenter");
        }
    }
}
