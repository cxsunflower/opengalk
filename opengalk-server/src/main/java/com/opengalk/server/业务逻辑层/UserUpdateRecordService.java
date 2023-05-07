package com.opengalk.server.业务逻辑层;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.实体类.UserUpdateRecord;

/**
 * @author cx
 * @description 针对表【用户操作记录】的数据库操作Service
 * @createDate 2023-03-13 15:40:36
 */
public interface UserUpdateRecordService extends IService<UserUpdateRecord> {

    UserUpdateRecord setUserUpdateRecordMapper(Long id, String tableName, String columnName, String beforeUpdate, String afterUpdate, Long updateBy, String remark);
}
