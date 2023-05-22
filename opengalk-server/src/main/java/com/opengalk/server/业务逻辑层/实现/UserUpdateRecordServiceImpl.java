package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.UserUpdateRecordService;
import com.opengalk.server.实体类.UserUpdateRecord;
import com.opengalk.server.数据访问层.UserUpdateRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author cx
 * @description 针对表【用户更新记录】的数据库操作Service实现
 * @createDate 2023-03-13 15:40:36
 */
@Service
public class UserUpdateRecordServiceImpl extends ServiceImpl<UserUpdateRecordMapper, UserUpdateRecord>
        implements UserUpdateRecordService {

    @Override
    public UserUpdateRecord setUserUpdateRecordMapper(Long id, String tableName, String columnName, String beforeUpdate, String afterUpdate, Long updateBy, String remark) {
        UserUpdateRecord record = new UserUpdateRecord();
        record.setId(id);
        record.setTableName(tableName);
        record.setColumnName(columnName);
        record.setBeforeUpdate(beforeUpdate);
        record.setAfterUpdate(afterUpdate);
        record.setUpdateBy(updateBy);
        record.setRemark(remark);

        return record;
    }
}




