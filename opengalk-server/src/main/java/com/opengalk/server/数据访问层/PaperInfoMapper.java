package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.GZSubjectObject;
import com.opengalk.server.实体类.PaperInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cx
 * @description 针对表【PaperInfo】的数据库操作Mapper
 * @createDate 2023-04-22 16:32:31
 * @Entity com.opengalk.server.实体类.PaperInfo
 */
@Mapper
public interface PaperInfoMapper extends BaseMapper<PaperInfo> {
    void addGZPaper(String uuid);

    void insertGZSubject(GZSubjectObject GZSubjectObject);

    void deleteGZPaper(String uuid);

    void setHasImgs(String uuid, Integer id);

    int getHasImgs(String uuid, Integer id);

    PaperInfo[] getPaperList(Long userId, Integer type);

    GZSubjectObject[] getGZPaperById(String uuid);

    GZSubjectObject[] getGZPaperByIdWithNoAnswer(String uuid);

    String[] getAnswerById(String uuid);

}
