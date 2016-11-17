package vn.edu.hcmut.its.tripmaester.controller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by thuanle on 12/16/15.
 */
public class ControllerCenter implements IController {
    private static ControllerCenter _INSTANCE;
    private Multimap<Class, IController> mController;
    private Multimap<Class, IInjectedCallback> mInjectedCallback;

    private ControllerCenter() {
        mController = HashMultimap.create();
        mInjectedCallback = HashMultimap.create();
    }

    public static ControllerCenter getInstance() {
        if (_INSTANCE == null) {
            synchronized (ControllerCenter.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new ControllerCenter();
                }
            }
        }
        return _INSTANCE;
    }

    public <R extends IRequest<? extends ICallback<D>>, D> void deliverData(R request, D result, Exception e) {
        try {
            request.getCallback().onCompleted(result, request.getTag(), e);
        } catch (Exception ignored) {
        }

        for (IInjectedCallback callback : mInjectedCallback.get(request.getClass())) {
            try {
                callback.onCompleted(request, result, e);
            } catch (Exception ignored) {
            }
        }
    }


    public void doRequest(IRequest request) {
        for (IController worker : mController.get(request.getClass())) {
            worker.handleRequestInBackground(request);
        }
    }

    @Override
    public Class[] getHandledRequests() {
        return new Class[0];
    }

    @Override
    public void handleRequestInBackground(IRequest request) {

    }

    @SuppressWarnings("unused")
    public void injectCallback(Class _class, IInjectedCallback callback) {
        mInjectedCallback.put(_class, callback);
    }

    public void registerController(IController controller) {
        for (Class key : controller.getHandledRequests()) {
            mController.put(key, controller);
        }
    }

    @SuppressWarnings("unused")
    public void unregisterController(IController controller) {
        for (Class key : controller.getHandledRequests()) {
            mController.remove(key, controller);
        }
    }

    @SuppressWarnings("unused")
    public void withdrawCallback(Class _class, IInjectedCallback callback) {
        mInjectedCallback.remove(_class, callback);
    }
}
