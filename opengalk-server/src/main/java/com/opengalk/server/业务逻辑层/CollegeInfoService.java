package com.opengalk.server.业务逻辑层;

import com.opengalk.server.实体类.CollegeInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.响应类.ResponseResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cx
 * @description 针对表【学校信息】的数据库操作Service
 * @createDate 2023-03-13 16:14:36
 */
public interface CollegeInfoService extends IService<CollegeInfo> {

    ResponseResult<?> getCollegeList(Integer currentPage, Integer pageSize, String condition, String keyword);

    ResponseResult<?> getCollegeList();

    ResponseResult<?> getCollegeInfoById(Long id);

    @Transactional
    ResponseResult<?> addCollege(CollegeInfo collegeInfo);

    @Transactional
    ResponseResult<?> updateCollegeInfo(CollegeInfo collegeInfo);

    ResponseResult<?> deleteCollegeById(Long id);
}
