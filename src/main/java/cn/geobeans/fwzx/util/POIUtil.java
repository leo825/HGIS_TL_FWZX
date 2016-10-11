package cn.geobeans.fwzx.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/11.
 */
public class POIUtil {

    private static Logger logger = Logger.getLogger(POIUtil.class);
    /***
     * 检查excel的合法性
     */
    public static Sheet checkSheetvalidity(String filePath) {
        Sheet sheet = null;
        FileInputStream fis = null;
        Workbook wookbook = null;
        //判断是否为excel类型文件
        if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
            try {
                //获取一个绝对地址的流
                fis = new FileInputStream(filePath);
            } catch (Exception e) {
                return null;
            }
            try {
                //2003版本的excel，用.xls结尾
                wookbook = new HSSFWorkbook(fis);//得到工作簿
            } catch (Exception ex) {
                try {
                    //这里需要重新获取流对象，因为前面的异常导致了流的关闭—————————————————————————————加了这一行
                    fis = new FileInputStream(filePath);
                    //2007版本的excel，用.xlsx结尾
                    wookbook = new XSSFWorkbook(filePath);//得到工作簿
                } catch (IOException e) {
                    return null;
                }
            }
            sheet = wookbook.getSheetAt(0); //得到一个工作表
            Row rowHead = sheet.getRow(0);//获得表头
            if (rowHead.getPhysicalNumberOfCells() != 4 || sheet.getLastRowNum() == 0) {//判断表头是否合格,这里看你有多少列,或者表为空
                logger.error("表头列数与规定的不符合，或者表里数据为空");
                return null;
            }
        } else {
            logger.error("文件不是excel类型");
        }
        return sheet;
    }
}
