package com.fuli.cloud.commons.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * ExcelUtil 基于easyExcel的开源框架，poi版本3.17 BeanCopy ExcelException 属于自定义数据，属于可自定义依赖
 * 工具类尽可能还是需要减少对其他java的包的依赖
 * https://github.com/wangxiaoxiongjuly/easy-excel-utils
 *
 * @author wenxuan.wang
 */
public abstract class ExcelUtil {

    // 5M
    private static final long MAX_SIZE = 1024 * 1024 * 5;

    /**
     * 私有化构造方法
     */
    private ExcelUtil() {
    }

    public static String getExportTmpPath() {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            System.out.println(System.getProperty("user.home"));
            return System.getProperty("user.home") + File.separator;
        }
        return "/tmp/";
    }

    /**
     * <pre>
     * Description: 读取Excel文件
     * </pre>
     *
     * @param excel                 文件
     * @param customContent         自定义参数
     * @param rowModel              实体类映射，继承 BaseRowModel 类
     * @param analysisEventListener 数据读取监听器
     * @author chenyi
     * @date 10:53 2019/8/1
     **/
    public static <T extends BaseRowModel, E extends AnalysisEventListener<T>> void readExcel(MultipartFile excel, Object customContent, Class<T> rowModel, E analysisEventListener) throws ExcelException {

        String fileName = excel.getOriginalFilename();
        if (fileName == null) {
            throw new ExcelException("文件格式错误！");
        }
        long size = excel.getSize();
        if (size > MAX_SIZE) {
            throw new ExcelException("文件过大！最大不能超过5M");
        }
        if (!fileName.toLowerCase().endsWith(ExcelTypeEnum.XLS.getValue()) && !fileName.toLowerCase().endsWith(ExcelTypeEnum.XLSX.getValue())) {
            throw new ExcelException("文件格式错误！");
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            ExcelReader reader = new ExcelReader(inputStream, customContent, analysisEventListener, true);
            reader.read(new Sheet(1, -1, rowModel));
//            for (Sheet sheet : reader.getSheets()) {
//                sheet.setHeadLineMun(-1);  // 第一行是标题，需要校验格式
//                sheet.setClazz(rowModel);
//                reader.read(sheet);
//            }
        } catch (IOException e) {
            e.printStackTrace();
            //do something
        }

    }

    /**
     * <pre>
     * Description: 读取Excel文件
     * </pre>
     *
     * @param filePath              文件路径
     * @param customContent         自定义参数
     * @param rowModel              实体类映射，继承 BaseRowModel 类
     * @param analysisEventListener 数据读取监听器
     * @author chenyi
     * @date 10:53 2019/8/1
     **/
    public static <T extends BaseRowModel, E extends AnalysisEventListener<T>> void readExcel(String filePath, Object customContent, Class<T> rowModel, E analysisEventListener) throws ExcelException {
        ExcelReader reader;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            reader = new ExcelReader(inputStream, customContent, analysisEventListener, true);
            for (Sheet sheet : reader.getSheets()) {
                sheet.setHeadLineMun(-1);  // 第一行是标题，需要校验格式
                sheet.setClazz(rowModel);
                reader.read(sheet);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //do something
        }

    }

    /**
     * 导出 Excel
     *
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @throws IOException
     */
    public static <T extends BaseRowModel> void writeDataToExcelFile(String fileName, List<T> list,
                                                                     String sheetName, ExcelTypeEnum excelTypeEnum, Class<T> classType)
            throws IOException {
        if (sheetName == null || "".equals(sheetName)) {
            sheetName = "sheet1";
        }
        OutputStream out = new FileOutputStream(fileName);
//		fileName = new String(URLEncoder.encode(fileName, "UTF-8")) + "-" + DateUtil.nowDateTime()
//		+ excelTypeEnum.getValue();
        if (excelTypeEnum == ExcelTypeEnum.XLSX) {
            ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null,
                    out, excelTypeEnum, true, new WriterHandler07<>(classType));
            Sheet sheet = new Sheet(1, 0, classType);
            sheet.setSheetName(sheetName);
            try {
                writer.write(list, sheet);
            } finally {
                writer.finish();
            }
            // 其实也可以专门调03版的样式，或者直接套用
        } else if (excelTypeEnum == ExcelTypeEnum.XLS) {
            ExcelWriterFactory writer = new ExcelWriterFactory(out, excelTypeEnum);
            Sheet sheet = new Sheet(1, 0, classType);
            sheet.setSheetName(sheetName);
            try {
                writer.write(list, sheet);
            } finally {
                if (writer != null) {
                    writer.finish();
                    writer.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }

    }

    /**
     * 导出 Excel ：一个 sheet，带表头 自定义WriterHandler 可以定制行列数据进行灵活化操作
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     */
    public static <T extends BaseRowModel> void writeExcel(HttpServletResponse response, List<T> list, String fileName,
                                                           String sheetName, ExcelTypeEnum excelTypeEnum, Class<T> classType) throws ExcelException {
        if (sheetName == null || "".equals(sheetName)) {
            sheetName = "sheet1";
        }
        if (excelTypeEnum == ExcelTypeEnum.XLSX) {
            ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null,
                    getOutputStream(fileName, response, excelTypeEnum), excelTypeEnum, true,
                    new WriterHandler07<>(classType));
            Sheet sheet = new Sheet(1, 0, classType);
            sheet.setSheetName(sheetName);
            try {
                writer.write(list, sheet);
            } finally {
                writer.finish();
            }
            // 其实也可以专门调03版的样式，或者直接套用
        } else if (excelTypeEnum == ExcelTypeEnum.XLS) {
            ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response, excelTypeEnum),
                    excelTypeEnum);
            Sheet sheet = new Sheet(1, 0, classType);
            sheet.setSheetName(sheetName);
            try {
                writer.write(list, sheet);
            } finally {
                writer.finish();
                writer.close();
            }
        }

    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response,
                                                          List<? extends BaseRowModel> list, String fileName, String sheetName, BaseRowModel object,
                                                          ExcelTypeEnum excelTypeEnum) throws ExcelException {
        ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response, excelTypeEnum),
                excelTypeEnum);
        Sheet sheet = new Sheet(1, 0, object.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        return writer;
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response,
                                                ExcelTypeEnum excelTypeEnum) throws ExcelException {
        // 创建本地文件
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8") + "-" + DateUtil.nowDateTime()
                    + excelTypeEnum.getValue();
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pragma", "No-cache");
            response.addHeader("Cache-Control", "No-cache");
            response.setDateHeader("Expires", 0);
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExcelException("创建文件失败！");
        }
    }

}
