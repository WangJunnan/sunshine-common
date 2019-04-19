package com.sunshine.common.util;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * ExcelUtils
 *
 * @author wangjn
 * @date 2019/4/19
 */
public class ExcelUtils {

    /**
     *   将excel内容输入到一个output流
     *   String sheetName = "用户信息";
     *   List<String> titleRow = new ArrayList<String>();
     *   titleRow.add("姓名");
     *   titleRow.add("年龄");
     *   List<List<String>> dataRows = new ArrayList<List<String>>();
     *   List<String> data1 =  new ArrayList<String>();
     *   data1.add("张三");
     *   data1.add("30");
     *   List<String> data2 =  new ArrayList<String>();
     *   data2.add("李四");
     *   data2.add("33");
     *   dataRows.add(data1);
     *   dataRows.add(data2);
     *   File file = ExcelUtils.createExcel(fileName, sheetName, titleRow, dataRows);
     *   if(file!=null){
     *       System.out.println(file.getName());
     *   }
     * @param out 需要输入的流
     * @param sheetName 表格名称
     * @param titleRow 标题行
     * @param dataRows 数据行
     * @return
     */
    public static void createExcelStream(OutputStream out, String sheetName, List<String> titleRow, List<List<String>> dataRows){
        if(sheetName==null){
            sheetName = "default";
        }
        Workbook wb = new SXSSFWorkbook(1000);
        SXSSFSheet sheet=(SXSSFSheet)wb.createSheet(sheetName);
        if(titleRow!=null){
            Row row=sheet.createRow(0);
            for(int i=0;i<titleRow.size();i++){
                Cell cell=row.createCell(i);
                cell.setCellValue(titleRow.get(i));
            }
        }
        if(dataRows!=null){
            for(int i=0;i<dataRows.size();i++){
                Row row=sheet.createRow(1+i);
                List<String> data = dataRows.get(i);
                if(data==null || data.size()==0){
                    continue;
                }
                for(int j=0;j<data.size();j++){
                    Cell cell=row.createCell(j);
                    cell.setCellValue(data.get(j));
                }
            }
        }
        try {
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
