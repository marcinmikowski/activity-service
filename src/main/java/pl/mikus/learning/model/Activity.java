package pl.mikus.learning.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@RequiredArgsConstructor
@Builder
@Getter
@ToString
public class Activity implements Serializable {
    private final String activity;
    private final String type;
    private final String participants;
    private final String price;
    private final String link;
    private final String key;
    private final String accessibility;
}
