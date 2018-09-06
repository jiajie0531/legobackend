package com.delllogistics.client.notice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Created by jiajie on 09/06/2017.
 */
@Data
@AllArgsConstructor
public class MultiMessageVariables {
    private String to;
    private Map vars;
}
