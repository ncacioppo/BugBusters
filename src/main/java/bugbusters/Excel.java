package bugbusters;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static java.lang.System.exit;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class Excel {

    public static final ArrayList<String> supportedFilesTypes = new ArrayList(Arrays.asList(new String[]{"xls", "xlsx", "csv"}));
    private Excel(){}

    public class csv {
        private csv(){ }

        public static Queue<ArrayList<String>> read(String fileName){
            ArrayList<String> rows = new ArrayList<>();

            try{
                File in = new File(fileName);
                Scanner myScan = new Scanner(in);
                while (myScan.hasNextLine()){
                    rows.add(myScan.nextLine());
                }
                myScan.close();
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }

            Queue<ArrayList<String>> out = new LinkedList<>();

            for (String x : rows){
                out.offer(new ArrayList<String>(Arrays.asList(x.split(","))));
            }

            return out;
        }

        public static boolean write(String fileName, Collection<Collection<String>> data){
            Queue<String> rows = new LinkedList<>();
            for (Collection x : data){
                String row = String.join(",", x);
                rows.offer(row);
            }

            try (PrintWriter writer = new PrintWriter(fileName)){
                for (String y : rows){
                    writer.write(y + "\n");
                }
                return true;
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<Collection<String>> data, ArrayList<String> header){
            String head = String.join(",", header);
            Queue<String> rows = new LinkedList<>();
            for (Collection x : data){
                String row = String.join(",", x);
                rows.offer(row);
            }

            try (PrintWriter writer = new PrintWriter(fileName)){
                writer.write(head + "\n");
                for (String y : rows){
                    writer.write(y + "\n");
                }
                return true;
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<String> data, String delimeter){
            return Excel.csv.write(fileName, convert(data, delimeter));
        }

        public static boolean write(String fileName, Collection<String> data, String delimeter, ArrayList<String> header){
            return Excel.csv.write(fileName, convert(data, delimeter), header);
        }
    }

    public class xls {
        private xls(){}

        public static Queue<ArrayList<String>> read(String fileName){


            Queue<ArrayList<String>> out = new LinkedList<>();

            try{
                FileInputStream file = new FileInputStream(fileName);

                HSSFWorkbook workbook = new HSSFWorkbook(file);

                HSSFSheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();
                while(rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    ArrayList<String> dataRow = new ArrayList<>();

                    Iterator<Cell> cellIterator = row.cellIterator();

                    for(int x=0; x<row.getLastCellNum(); x++){
                        Cell cell = row.getCell(x, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                                cell.setCellType(STRING);;
                                dataRow.add(cell.getStringCellValue());
                                break;
                            case STRING:
                                dataRow.add(cell.getStringCellValue());
                                break;
                            default:
                                dataRow.add("");
                                break;
                        }
                    }
                    out.offer(dataRow);
                }
                file.close();
                return out;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        public static boolean write(String fileName, Collection<Collection<String>> data){

            HSSFWorkbook workbook = new HSSFWorkbook();

            String[] sheetName = fileName.split("\\\\");
            HSSFSheet sheet = workbook.createSheet(((sheetName[sheetName.length-1]).split("\\."))[0]);

            int rownum = 0;
            for (Collection<String> dataRow : data){
                Row row = sheet.createRow(rownum++);
                int cellnum = 0;
                for (String str : dataRow){
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(str);
                }
            }
            try{
                FileOutputStream out = new FileOutputStream(fileName);
                workbook.write(out);
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<Collection<String>> data, ArrayList<String> header){
            HSSFWorkbook workbook = new HSSFWorkbook();

            String[] sheetName = fileName.split("\\\\");
            HSSFSheet sheet = workbook.createSheet(((sheetName[sheetName.length-1]).split("\\."))[0]);

            Row row = sheet.createRow(0);
            int cellnum = 0;
            for (String str : header){
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(str);
            }

            int rownum = 1;
            for (Collection<String> dataRow : data){
                row = sheet.createRow(rownum++);
                cellnum = 0;
                for (String str : dataRow){
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(str);
                }
            }
            try{
                FileOutputStream out = new FileOutputStream(fileName);
                workbook.write(out);
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<String> data, String delimeter){
            return Excel.xls.write(fileName, convert(data, delimeter));
        }

        public static boolean write(String fileName, Collection<String> data, String delimeter, ArrayList<String> header){
            return Excel.xls.write(fileName, convert(data, delimeter), header);
        }
    }

    public class xlsx {
        private xlsx(){}

        public static Queue<ArrayList<String>> read(String fileName){

            Queue<ArrayList<String>> out = new LinkedList<>();

            try{
                FileInputStream file = new FileInputStream(fileName);

                XSSFWorkbook workbook = new XSSFWorkbook(file);

                XSSFSheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();
                while(rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    ArrayList<String> dataRow = new ArrayList<>();

                    Iterator<Cell> cellIterator = row.cellIterator();
                    for(int x=0; x<row.getLastCellNum(); x++){
                        Cell cell = row.getCell(x, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        switch (cell.getCellType())
                        {
                            case NUMERIC:
                                cell.setCellType(STRING);;
                                dataRow.add(cell.getStringCellValue());
                                break;
                            case STRING:
                                dataRow.add(cell.getStringCellValue());
                                break;
                            default:
                                dataRow.add("");
                                break;
                        }
                    }
                    out.offer(dataRow);
                }
                file.close();
                return out;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        public static boolean write(String fileName, Collection<Collection<String>> data){

            SXSSFWorkbook workbook = new SXSSFWorkbook();

            String[] sheetName = fileName.split("\\\\");
            SXSSFSheet sheet = workbook.createSheet(((sheetName[sheetName.length-1]).split("\\."))[0]);

            int rownum = 0;
            for (Collection<String> dataRow : data){
                Row row = sheet.createRow(rownum++);
                int cellnum = 0;
                for (String str : dataRow){
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(str);
                }
            }
            try{
                FileOutputStream out = new FileOutputStream(fileName);
                workbook.write(out);
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<Collection<String>> data, ArrayList<String> header){
            SXSSFWorkbook workbook = new SXSSFWorkbook();

            String[] sheetName = fileName.split("\\\\");
            SXSSFSheet sheet = workbook.createSheet(((sheetName[sheetName.length-1]).split("\\."))[0]);

            Row row = sheet.createRow(0);
            int cellnum = 0;
            for (String str : header){
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(str);
            }

            int rownum = 1;
            for (Collection<String> dataRow : data){
                row = sheet.createRow(rownum++);
                cellnum = 0;
                for (String str : dataRow){
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(str);
                }
            }
            try{
                FileOutputStream out = new FileOutputStream(fileName);
                workbook.write(out);
                out.close();
                return true;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        public static boolean write(String fileName, Collection<String> data, String delimeter) {
            return Excel.xlsx.write(fileName, convert(data, delimeter));
        }

        public static boolean write(String fileName, Collection<String> data, String delimiter, ArrayList<String> header){
            return Excel.xlsx.write(fileName, convert(data, delimiter), header);
        }
    }

    private static Collection<Collection<String>> convert(Collection<String> data, String delimeter){

        ArrayList<Collection<String>> out = new ArrayList<>();

        for (String x : data){
            List<String> row =  Arrays.asList(x.split(delimeter));
            out.add(row);
        }

        return out;
    }

    public static Queue<ArrayList<String>> read(String fileName){

        String fileExtension = fileName.split("\\.")[1];

        if (fileExtension.equalsIgnoreCase("CSV")){
            return Excel.csv.read(fileName);
        } else if (fileExtension.equalsIgnoreCase("XLS")){
            return Excel.xls.read(fileName);
        } else if (fileExtension.equalsIgnoreCase("XLSX")){
            return Excel.xlsx.read(fileName);
        } else {
            System.out.println("This file type is not currently supported.\nThe supported file types are currently:\n" + supportedFilesTypes);
            exit(1);
            return null;
        }
    }

    public static boolean write(String fileName, Collection<Collection<String>> data){

        String fileExtension = fileName.split("\\.")[1];

        if (fileExtension.equalsIgnoreCase("CSV")){
            return Excel.csv.write(fileName, data);
        } else if (fileExtension.equalsIgnoreCase("XLS")){
            return Excel.xls.write(fileName, data);
        } else if (fileExtension.equalsIgnoreCase("XLSX")){
            return Excel.xlsx.write(fileName, data);
        } else {
            System.out.println("This file type is not currently supported.\nThe supported file types are currently:\n" + supportedFilesTypes);
            exit(1);
            return false;
        }
    }

    public static boolean write(String fileName, Collection<Collection<String>> data, ArrayList<String> header){
        String fileExtension = fileName.split("\\.")[1];

        if (fileExtension.equalsIgnoreCase("CSV")){
            return Excel.csv.write(fileName, data, header);
        } else if (fileExtension.equalsIgnoreCase("XLS")){
            return Excel.xls.write(fileName, data, header);
        } else if (fileExtension.equalsIgnoreCase("XLSX")){
            return Excel.xlsx.write(fileName, data, header);
        } else {
            System.out.println("This file type is not currently supported.\nThe supported file types are currently:\n" + supportedFilesTypes);
            exit(1);
            return false;
        }
    }

    public static boolean write(String fileName, Collection<String> data, String delimeter){

        String fileExtension = fileName.split(".")[1];

        if (fileExtension.equalsIgnoreCase("CSV")){
            return Excel.csv.write(fileName, data, delimeter);
        } else if (fileExtension.equalsIgnoreCase("XLS")){
            return Excel.xls.write(fileName, data, delimeter);
        } else if (fileExtension.equalsIgnoreCase("XLSX")){
            return Excel.xlsx.write(fileName, data, delimeter);
        } else {
            System.out.println("This file type is not currently supported.\nThe supported file types are currently:\n" + supportedFilesTypes);
            exit(1);
            return false;
        }
    }

    public static boolean write(String fileName, Collection<String> data, String delimeter, ArrayList<String> header){

        String fileExtension = fileName.split(".")[1];

        if (fileExtension.equalsIgnoreCase("CSV")){
            return Excel.csv.write(fileName, data, delimeter, header);
        } else if (fileExtension.equalsIgnoreCase("XLS")){
            return Excel.xls.write(fileName, data, delimeter, header);
        } else if (fileExtension.equalsIgnoreCase("XLSX")){
            return Excel.xlsx.write(fileName, data, delimeter, header);
        } else {
            System.out.println("This file type is not currently supported.\nThe supported file types are currently:\n" + supportedFilesTypes);
            exit(1);
            return false;
        }
    }
}
