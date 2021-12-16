package com.wissensalt.searchengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private static final long serialVersionUID = -7601467131496411137L;

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
}
