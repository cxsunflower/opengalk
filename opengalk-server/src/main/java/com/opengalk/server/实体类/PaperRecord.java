package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName paper_record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "paper_record", schema = "\"paper\"")
public class PaperRecord implements Serializable {
    /**
     *
     */
    private String id;

    /**
     *
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     *
     */
    @TableField(value = "score")
    private Integer score;

    /**
     *
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "answer")
    private String answer;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}