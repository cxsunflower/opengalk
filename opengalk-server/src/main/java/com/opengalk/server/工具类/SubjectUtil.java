package com.opengalk.server.工具类;

import com.opengalk.server.实体类.GZSubjectObject;
import com.opengalk.server.实体类.SubjectObject;
import com.opengalk.server.数据访问层.PaperInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.File;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class SubjectUtil {

    private final PaperInfoMapper paperInfoMapper;

    private final ImageUtil imageUtil;

    public int addGZPaper(SubjectObject[] subjectArray, String uuid, final String imgDir) {
        for (int i = 0; i < subjectArray.length; i++) {
            // 当前题目
            SubjectObject tmp = subjectArray[i];
            GZSubjectObject gzSubjectObject = GZSubjectObject.builder()
                    .uuid(uuid)
                    .id(i)
                    .type(tmp.getType())
                    .subject(tmp.getSubject().getContent())
                    .optionA(String.join("", tmp.getItems()[0].getText()))
                    .optionB(String.join("", tmp.getItems()[1].getText()))
                    .optionC(String.join("", tmp.getItems()[2].getText()))
                    .optionD(String.join("", tmp.getItems()[3].getText()))
                    .answer(String.join("", tmp.getAnswer()))
                    .build();

            paperInfoMapper.insertGZSubject(gzSubjectObject);
            if (!ObjectUtils.isEmpty(tmp.getSubject().getImgs()) && paperInfoMapper.getHasImgs(uuid, i) == 0) {
                paperInfoMapper.setHasImgs(uuid, i);
            }

            // 题目图片
            for (int j = 0; j < tmp.getSubject().getImgs().length; j++) {
                String img = tmp.getSubject().getImgs()[j];
                // 上传文件的路径
                String filePath = imgDir + uuid + "/" + "subject_" + i + "_" + j;
                File fileDir = new File(filePath);

                // 如果没有文件夹则建一个
                if (!fileDir.getParentFile().exists() && fileDir.getParentFile().mkdirs()) {
                    log.info("创建文件夹成功：" + fileDir.getAbsolutePath());
                }
                if (imageUtil.Base64ToImage(img, filePath) == 0) {
                    return 0;
                }
            }

            // 选项图片
            for (int j = 0; j < tmp.getItems().length; j++) {
                SubjectObject.SelectionObject selection = tmp.getItems()[j];

                if (!ObjectUtils.isEmpty(selection.getImgs()) && paperInfoMapper.getHasImgs(uuid, i) == 0) {
                    paperInfoMapper.setHasImgs(uuid, i);
                }

                for (int k = 0; k < selection.getImgs().length; k++) {
                    String img = selection.getImgs()[k];
                    // 上传文件的路径
                    String filePath = imgDir + uuid + "/" + "subject_" + i + "_" + j + "_" + k;
                    File fileDir = new File(filePath);

                    // 如果没有文件夹则建一个
                    if (!fileDir.getParentFile().exists() && fileDir.getParentFile().mkdirs()) {
                        log.info("创建文件夹成功：" + fileDir.getAbsolutePath());
                    }
                    if (imageUtil.Base64ToImage(img, filePath) == 0) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }
}
