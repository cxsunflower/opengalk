package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GZSubjectObject {

    @Size(min = 32, max = 32)
    @TableField(exist = false)
    private String uuid;

    @Min(1)
    @Max(100)
    @TableId(value = "id")
    private Integer id;

    /**
     * 0单选，1多选
     */
    @Min(0)
    @Max(1)
    @TableField(value = "type")
    private Integer type;

    @TableField(value = "subject")
    private String subject;

    @TableField(value = "picture")
    private String picture;

    @TableField(value = "option_a")
    private String optionA;

    @TableField(value = "option_b")
    private String optionB;

    @TableField(value = "option_c")
    private String optionC;

    @TableField(value = "option_d")
    private String optionD;

    @Size(max = 4)
    @TableField(value = "answer")
    private String answer;
}
