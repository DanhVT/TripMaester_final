package vn.edu.hcmut.its.tripmaester.controller;

import vn.edu.hcmut.its.tripmaester.service.http.HttpControllerCenter;

/**
 * Created by thuanle on 12/17/15.
 */
public class ControllerFactory {
    /**
     * Create concrete controllers and register to ControllerCenter
     */
    public static void createControllers() {
        // TODO: 12/17/15 Vu Lam
        HttpControllerCenter.getInstance().registerControllers();
    }
}
