package com.opengalk.server.实体类;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subject {
    /**
     * 类型，0为单选，1为多选
     */
    @Min(0)
    @Max(1)
    private int type;
    private String subject;
    private String[] answer;
    private SelectionObject[] items;
    private String error;

    @Data
    public static class SelectionObject {
        private String picture;
        private String text;
        @Size(max = 1)
        private String value;
    }

}
