package student.examples.uservice.business.dto;

import lombok.Getter;

@Getter
public class RestSuccessResponse extends RestResponse{
    private Object body;

    public RestSuccessResponse(int statusCode, Object body){
        super(statusCode, "success");
        this.body = body;
    }
}
