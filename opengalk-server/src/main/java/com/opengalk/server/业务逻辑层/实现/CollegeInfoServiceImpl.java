package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.CollegeInfoService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.CollegeInfo;
import com.opengalk.server.数据访问层.CollegeInfoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cx
 * @description 针对表【学校信息】的数据库操作Service实现
 * @createDate 2023-03-13 16:14:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CollegeInfoServiceImpl extends ServiceImpl<CollegeInfoMapper, CollegeInfo>
        implements CollegeInfoService {

    private final CollegeInfoMapper collegeInfoMapper;

    @Override
    public ResponseResult<?> getCollegeList(Integer currentPage, Integer pageSize, String condition, @NotNull String keyword) {
        Page<CollegeInfo> page = new Page<>(currentPage, pageSize);
        QueryWrapper<CollegeInfo> queryWrapper = new QueryWrapper<>();

        if (!keyword.isBlank() && !"id".equals(condition)) {
            queryWrapper.like(condition, keyword);
        } else if (!keyword.isBlank() && "id".equals(condition)) {
            long id;
            try {
                id = Long.parseLong(keyword);
            } catch (Exception e) {
                return new ResponseResult<>(0, "异常id参数", null);
            }

            queryWrapper.like("CAST(id AS VARCHAR)", id);
        }
        return new ResponseResult<>(1, null, collegeInfoMapper.selectPage(page, queryWrapper));
    }

    @Override
    public ResponseResult<?> getCollegeList() {
        QueryWrapper<CollegeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "college_name");

        return new ResponseResult<>(1, null, collegeInfoMapper.selectList(queryWrapper));
    }

    @Override
    public ResponseResult<?> getCollegeInfoById(Long id) {
        QueryWrapper<CollegeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        CollegeInfo collegeInfo = collegeInfoMapper.selectOne(queryWrapper);
        return new ResponseResult<>(1, null, collegeInfo);
    }

    @Transactional
    @Override
    public ResponseResult<?> addCollege(@NotNull CollegeInfo collegeInfo) {
        log.info(collegeInfo.toString());
        QueryWrapper<CollegeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("college_name", collegeInfo.getCollegeName());

        if (collegeInfoMapper.selectOne(queryWrapper) == null) {
            CollegeInfo newCollege = CollegeInfo.builder()
                    .collegeName(collegeInfo.getCollegeName())
                    .remark(collegeInfo.getRemark())
                    .build();

            collegeInfoMapper.insert(newCollege);
            return new ResponseResult<>(1, "添加学校成功", null);
        }
        return new ResponseResult<>(0, "学校名称重复", null);
    }

    @Transactional
    @Override
    public ResponseResult<?> updateCollegeInfo(@NotNull CollegeInfo collegeInfo) {
        CollegeInfo updateCollegeInfo = CollegeInfo.builder()
                .id(collegeInfo.getId())
                .collegeName(collegeInfo.getCollegeName())
                .remark(collegeInfo.getRemark())
                .build();

        if (collegeInfoMapper.updateById(updateCollegeInfo) == 1) {
            return new ResponseResult<>(1, "更新学校信息成功", null);
        }
        return new ResponseResult<>(0, "更新学校信息失败", null);
    }

    @Override
    public ResponseResult<?> deleteCollegeById(Long id) {
        if (collegeInfoMapper.deleteById(id) == 1) {
            return new ResponseResult<>(1, "删除成功", null);
        }
        return new ResponseResult<>(1, "删除失败", null);
    }
}




