package vn.edu.hcmut.its.tripmaester.controller;

public interface IInjectedCallback<C extends ICallback<D>, D> {
    void onCompleted(D data, Object tag, Exception e);
}
