package br.com.avcar.oficina.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String title;
    private final String motive;
    private final String severity;
    private final List<String> details = new ArrayList<>();

    public ErrorResponse(String title, String motive, String severity) {
        this.title = title;
        this.motive = motive;
        this.severity = severity;
    }

    public void addDetail(String detail) {
        if (detail != null && !detail.isBlank()) {
            details.add(detail);
        }
    }
}
