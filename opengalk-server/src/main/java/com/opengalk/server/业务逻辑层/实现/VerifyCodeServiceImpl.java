package com.opengalk.server.业务逻辑层.实现;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.工具类.VerifyCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("verifyCodeService")
@RequiredArgsConstructor
public class VerifyCodeServiceImpl {

    public final StringRedisTemplate stringRedisTemplate;

    public ResponseResult<?> getVerificationCode() {
        String uuid = UUID.fastUUID().toString();

        // 利用图片工具生成图片
        // 返回的数组第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyCodeUtil.newBuilder()
                .setWidth(120)   //设置图片的宽度
                .setHeight(35)   //设置图片的高度
                .setSize(6)      //设置字符的个数
                .setLines(10)    //设置干扰线的条数
                .setFontSize(25) //设置字体的大小
                .setTilt(true)   //设置是否需要倾斜
                .setBackgroundColor(Color.LIGHT_GRAY) //设置验证码的背景颜色
                .build()         //构建VerifyCodeUtil项目
                .createImage();  //生成图片
        // 打印验证码
        log.info("验证码:" + objs[0]);

        stringRedisTemplate.opsForValue().set(("VERIFICATION_CODE_" + uuid), (String) objs[0], 3, TimeUnit.MINUTES);

        BufferedImage image = (BufferedImage) objs[1];
        //io流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //写入流中
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //转换成字节
        byte[] bytes = baos.toByteArray();
        //转换成base64串
        String base64 = Base64Encoder.encode(bytes);

        return new ResponseResult<>(1, null, new String[]{base64, uuid});
    }

    /**
     * 校验验证码
     *
     * @param uuid             uuid
     * @param verificationCode 验证码
     * @return 0验证码正确，1验证码错误，2验证码失效
     */
    public int verifyVerificationCode(String uuid, String verificationCode) {
        log.info("当前验证码：" + verificationCode);
        String key = "VERIFICATION_CODE_" + uuid;

        String answer;

        try {
            answer = Objects.requireNonNull(stringRedisTemplate.opsForValue().get(key));
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }

        if (!StringUtils.hasText(answer)) {
            return 2;
        }

        // 校验验证码，忽略大小写
        if (!verificationCode.equalsIgnoreCase(answer)) {
            stringRedisTemplate.delete(key);
            return 1;
        }

        // 验证通过之后手动将验证码失效
        stringRedisTemplate.delete(key);

        return 0;
    }

    /**
     * 返回校验验证码结果
     *
     * @param uuid             uuid
     * @param verificationCode 验证码
     * @return ResponseResult
     */
    public ResponseResult<?> verifiedResponse(String uuid, String verificationCode) {
        int status = verifyVerificationCode(uuid, verificationCode);
        return switch (status) {
            case 0 -> null;
            case 1 -> new ResponseResult<>(2, "验证码错误", null);
            case 2 -> new ResponseResult<>(2, "验证码失效", null);
            default -> new ResponseResult<>(2, "验证异常", null);
        };
    }
}
