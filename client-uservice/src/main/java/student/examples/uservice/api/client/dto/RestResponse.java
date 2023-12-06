package student.examples.uservice.api.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestResponse {

    private int statusCode;
    private String statusMessage;

}
