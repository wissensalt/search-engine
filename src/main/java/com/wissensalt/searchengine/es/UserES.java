package com.wissensalt.searchengine.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "search-user-idx")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserES {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "first_name")
    private String firstName;

    @Field(type = FieldType.Text, name = "middle_name")
    private String middleName;

    @Field(type = FieldType.Text, name = "last_name")
    private String lastName;
}
