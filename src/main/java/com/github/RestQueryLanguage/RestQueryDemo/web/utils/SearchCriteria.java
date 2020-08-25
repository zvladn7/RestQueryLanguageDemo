package com.github.RestQueryLanguage.RestQueryDemo.web.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    //to show predicates like this one: age > 25

    //used to hold field name - for example: firstName, age, ... etc
    private String key;

    //used to hold the operation - for example: equality, less than, ... etc
    private String operation;

    //used to hold the field value - for example: John, 25, .. etc
    private Object value;

}
