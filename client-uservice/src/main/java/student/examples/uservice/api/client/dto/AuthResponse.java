package student.examples.uservice.api.client.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String status;
    private Object body;
    public AuthResponse(String status, Object body) {
        super();
        this.status = status;
        this.body = body;
    }




}
