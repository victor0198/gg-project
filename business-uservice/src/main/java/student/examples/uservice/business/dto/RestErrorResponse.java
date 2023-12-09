package student.examples.uservice.business.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class RestErrorResponse extends RestResponse{
    private Map<String, String> errors;

    public RestErrorResponse(int statusCode, Map<String, String> errors){
        super(statusCode, "failed");
        this.errors = errors;
    }
}
