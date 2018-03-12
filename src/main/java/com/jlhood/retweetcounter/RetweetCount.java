package com.jlhood.retweetcounter;

import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetweetCount {
    private String username;
    private long retweetCount;
    private long favoriteCount;
}
