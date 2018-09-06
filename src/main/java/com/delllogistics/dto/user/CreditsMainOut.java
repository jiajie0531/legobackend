package com.delllogistics.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreditsMainOut {
    private long totalScore;
    private List<CreditsOut> credits;
}
