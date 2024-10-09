package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import org.example.enums.RequestStatus;

import java.util.Date;

@Getter
@Builder
public class Request {
    private Integer id;
    private Date requestTime;
    private RequestStatus requestStatus;
}
