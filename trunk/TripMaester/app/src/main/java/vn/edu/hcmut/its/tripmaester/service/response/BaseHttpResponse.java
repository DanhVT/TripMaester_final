package vn.edu.hcmut.its.tripmaester.service.response;

/**
 * Created by thuanle on 12/18/15.
 */
public class BaseHttpResponse {
    private String description;
    private int code;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
