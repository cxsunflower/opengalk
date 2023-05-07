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
 * @TableName paper_info
 */
@TableName(value = "paper_info", schema = "\"paper\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaperInfo implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 试卷id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 试卷
     */
    @TableField(value = "type")
    private Integer type;
    /**
     * 试卷名称
     */
    @TableField(value = "name")
    @Size(min = 3, max = 20)
    private String name;
    /**
     * 试卷描述
     */
    @TableField(value = "remark")
    @Size(min = 3, max = 200)
    private String remark;
    /**
     * 试卷创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 由谁创建
     */
    @TableField(value = "create_by")
    private Long createBy;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private String teacherName;

    @TableField(exist = false)
    private Subject[] subjectArray;

    @TableField(exist = false)
    private Integer score;
}