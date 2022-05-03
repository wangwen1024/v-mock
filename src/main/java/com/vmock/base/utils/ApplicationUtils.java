package com.vmock.base.utils;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;

import java.security.CodeSource;

import static com.vmock.base.utils.DataMigrationUtils.dataMigrationCheck;

/**
 * 应用程序相关
 *
 * @author vt
 */
@UtilityClass
public class ApplicationUtils {

    /**
     * 启动主程序
     *
     * @param primarySource 启动类
     * @param args          参数
     */
    @SneakyThrows
    public static void run(Class<?> primarySource, String... args) {
        // 修改java.io.tmpdir为jar所在的目录，防止sqlite文件默认在temp下的丢失
        // https://github.com/xerial/sqlite-jdbc/issues/489
        CodeSource codeSource = primarySource.getProtectionDomain().getCodeSource();
        String pathname = codeSource.getLocation().toURI().toString();
        // 去除协议部分jar: or file:
        String backPart = StrUtil.subAfter(pathname, ":", true);
        String workplaceDirStr = StrUtil.subBefore(backPart, ".jar", false);
        System.setProperty("java.io.tmpdir", workplaceDirStr);
        // 是否需要数据迁移检测
        dataMigrationCheck();
        // 启动主程序
        SpringApplication.run(primarySource, args);
    }
}
