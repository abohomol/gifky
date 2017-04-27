package com.abohomol.gifky.skeleton;

/**
 * Every presenter in the app must either implement this interface or extend
 * indicating the view type that wants to be attached with.
 */
public interface Presenter<V extends View> {

    void attachView(V view);

    void detachView();
}
