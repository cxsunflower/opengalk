package com.opengalk.server.工具类;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class ImageUtil {
    /**
     * base64转图片格式并写入filePath
     *
     * @param base64
     * @param filePath
     * @return 0失败，1成功
     */
    public int Base64ToImage(String base64, final String filePath) {
        try {
            // 去掉base64前缀 data:image/jpg;base64,
            base64 = base64.substring(base64.indexOf(",", 1) + 1);
            byte[] b = Base64.decode(base64);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 保存图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(b);
            out.flush();
            out.close();
            return 1;
        } catch (IOException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return 0;
        }
    }

    /**
     * 图片转base64
     *
     * @param filePath
     * @return Base64
     */
    public String ImageToBase64(final String filePath){
        byte[] data;
        // 读取图片字节数组
        try (InputStream in = new FileInputStream(filePath)) {
            data = new byte[in.available()];
            int counts = in.read(data);
            log.info("I/O字节：" + counts);
            return Base64Encoder.encode(data);
        } catch (IOException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return null;
        }
    }
}
