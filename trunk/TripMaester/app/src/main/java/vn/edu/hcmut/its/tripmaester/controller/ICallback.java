package vn.edu.hcmut.its.tripmaester.controller;

/**
 * Callback on request complete
 *
 * @param <T>
 */
public interface ICallback<T> {
    void onCompleted(T data, Object tag, Exception e);
}
