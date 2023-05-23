package com.vodafone.model;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Links {
    private String rel;
    private String href;
}
