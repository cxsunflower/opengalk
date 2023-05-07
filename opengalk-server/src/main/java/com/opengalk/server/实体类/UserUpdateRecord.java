package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user_update_record
 */
@TableName(value = "user_update_record",schema = "\"user\"")
@Data
public class UserUpdateRecord implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 修改哪个表
     */
    @TableField(value = "table_name")
    private String tableName;
    /**
     * 修改哪一列
     */
    @TableField(value = "column_name")
    private String columnName;
    /**
     * 更新之前数据
     */
    @TableField(value = "before_update")
    private String beforeUpdate;
    /**
     * 更新之后数据
     */
    @TableField(value = "after_update")
    private String afterUpdate;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT)
    private LocalDateTime updateTime;
    /**
     * 由谁更新
     */
    @TableField(value = "update_by")
    private Long updateBy;
    /**
     * 更新备注
     */
    @TableField(value = "remark")
    private String remark;
}