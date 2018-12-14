package com.zccbh.util.export;

import com.zccbh.demand.pojo.model.ExcelFieldEntity;
import org.apache.poi.hssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelExport {

    public static void exportExcel(String title, String[] headers, Collection<ExcelFieldEntity> dataset, OutputStream out, String pattern) {
        HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
        HSSFSheet sheet = workbook.createSheet(title);// 生成一个表格

        HSSFRow row = sheet.createRow(0);//产生表格标题行
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(headers[i]));
        }
        if (null != dataset) {
            Iterator<ExcelFieldEntity> it = dataset.iterator();//遍历集合数据，产生数据行
            int index = 0;
            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                ExcelFieldEntity t = (ExcelFieldEntity) it.next();
                //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (short i = 0; i < fields.length; i++) {
                    HSSFCell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                        Object value = getMethod.invoke(t, new Object[]{});
                        String textValue = null;//判断值的类型后进行强制类型转换
                        if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else {
                            if (value != null) //其它数据类型都当作字符串简单处理
                                textValue = value.toString();
                        }
                        if (textValue != null) {//如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                            Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if (matcher.matches()) {//是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                                cell.setCellValue(richString);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
