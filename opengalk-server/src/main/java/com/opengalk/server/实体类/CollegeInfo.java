package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName college_info
 */
@TableName(value = "college_info", schema = "\"college\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollegeInfo implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 学校id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学校名称
     */
    @Size(min = 1, max = 20)
    @TableField(value = "college_name")
    private String collegeName;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 备注
     */
    @Size(max = 200)
    @TableField(value = "remark")
    private String remark;

}