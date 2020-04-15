package com.cqi.hr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FileUtils {
	public static final String[] getFileName(String filePath) {
		if(filePath==null || filePath.length()==0) return null;
		
		int slashIndex = filePath.lastIndexOf('/');
		if(slashIndex==-1) slashIndex = filePath.lastIndexOf('\\');
		if(slashIndex!=-1) filePath = filePath.substring(slashIndex+1);
		
		int dotIndex = filePath.lastIndexOf('.');
		if(dotIndex==-1) return new String[] {filePath, null};

		return new String[] {filePath.substring(0, dotIndex), filePath.substring(dotIndex+1)};
	}
	
	/**
	 * 取得路徑字串左邊與右邊沒有斜線的位置
	 * @param path
	 * @return
	 */
	private static final int[] getNoSlashIndices(String path) {
		int[] indices = new int[2];
		
		char c;
		for(int i=0, len=path.length();i<len;++i) {
			c = path.charAt(i);
			if(c!='/' && c!='\\') {
				indices[0] = i;
				break;
			}
		}
		
		for(int i=path.length()-1;i>=0;--i) {
			c = path.charAt(i);
			if(c!='/' && c!='\\') {
				indices[1] = i;
				break;
			}
		}
		
		return indices;
	}
	public static final String combinePath(String prefixPath, String... suffixPaths) {
		StringBuilder sb = new StringBuilder();
		
		int[] indices;
		if(prefixPath!=null && prefixPath.length()>0) {
			indices = getNoSlashIndices(prefixPath);
			sb.append(prefixPath.substring(0, indices[1]+1));
		}
		
		for(String path:suffixPaths) {
			if(path!=null && path.length()>0) {
				indices = getNoSlashIndices(path);
				sb.append("/");
				sb.append(path.substring(indices[0], indices[1]+1));
			}
		}

		return sb.toString();
	}
	
	public void doPropertytoTxtFile(String src,String target) {
		Properties p = new Properties();
		PrintWriter pw = null;
		try {
			p.load(new FileInputStream(new File(src)));
			TreeSet<String> keys = new TreeSet<String>();
			
			for(Object o : p.keySet()){
				keys.add(o.toString());
			}
			
			pw = new PrintWriter(new OutputStreamWriter( new FileOutputStream(new File(target))));
			for(String key : keys){
				String line = key + "=" + p.getProperty(key);
				pw.println(line);
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(pw != null){
				try {
					pw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public static void main(String[] args) {
		//filterScript();
		changeExcel();
		System.out.println("end.");
	}
	public static void filterScript(){
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("ACT_FUNCTION",0);
		map.put("ACT_ROLE",0);
		map.put("ACT_ROLE_FUNCTION",0);
		map.put("ADDR_CITY",0);
		map.put("ADDR_COUNTY",0);
		map.put("ADDR_PROVINCE",0);

		map.put("ALARM_CONFIG_BIO",0);
		map.put("ALARM_CONFIG_HEALTHY_GROUP_BIO",0);

		map.put("COMM_DIET_PERIOD",0);
		map.put("COMM_HEALTHY_GROUP",0);
		map.put("COMM_HEALTHY_GROUP_CLASSIFIED_CONFIG",0);
		map.put("COMM_HEALTHY_GROUP_CLASSIFIED_CONFIG_FRAMINGHAM",0);

		map.put("COMM_PARAMETER",0);
		map.put("DEVICE_SUPPORT",0);
		map.put("FOOD",0);
		map.put("FOOD_PACKAGE",0);
		map.put("FOOD_PACKAGE_DETAIL",0);
		map.put("FOOD_PACKAGE_DIET_PERIOD",0);
		map.put("FOOD_PACKAGE_HEALTHY_GROUP",0);

		map.put("FOOD_TYPE",0);

		map.put("HEALTH_CHECK_COLOR_LABEL",0);
		map.put("HEALTH_CHECK_ITEM",0);
		map.put("HEALTH_CHECK_TYPE",0);
		//map.put("NEWS",0);
		map.put("NEWS_GROUP",0);

		//map.put("PUBLISH",0);
		map.put("PUBLISH_DISPLAY_TYPE",0);
		map.put("PUBLISH_TYPE",0);

		map.put("QUESTIONARY",0);
		map.put("QUESTIONARY_DETAIL",0);

		map.put("REF_DEPARTMENT",0);
		map.put("REF_DISEASE",0);
		map.put("REF_RELATIONSHIP",0);
		map.put("REF_VACCINATION",0);
		map.put("SPORT",0);
		File sqlFile = new File("C:/tmp/input_data.sql");
		File newSqlFile = new File("C:/tmp/output_data.sql");
		BufferedReader br = null;
		PrintWriter pw = null;
		String line = null;
		String INSERT = "insert into ";
		String VALUES = "values";
		String table = null;
		String preTable = null;
		boolean isEnd = true;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile)));
			pw = new PrintWriter(new FileOutputStream(newSqlFile));
			while((line = br.readLine()) != null){
				if(line.indexOf("insert into") != -1 && line.indexOf("values") != -1){
					table = line.substring(line.indexOf(INSERT) + INSERT.length(),line.indexOf(VALUES)).trim().toUpperCase();
					if(preTable != null && !table.equals(preTable)){
						pw.println("Set Identity_Insert " + preTable + " OFF");
						preTable = null;
					}
					if(map.get(table) != null){
						Integer seq = map.get(table);
						
						if(line.indexOf("( default") != -1){
							if(seq == 0){
								pw.println("Set Identity_Insert " + table + " ON");
							}
							seq++;
							line = line.replace("( default","("+ seq.toString());
							map.put(table, seq);
							preTable = table;
						}
						
						
						pw.println(line);
						if(line.indexOf(";") != -1){
							isEnd = true;
						}else{
							isEnd = false;
						}
						
					}
					
				}else if(!isEnd){
					pw.println(line);
					if(line.indexOf(";") != -1){
						isEnd = true;
					}
				}
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(br != null){try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
			if(pw != null){try {
				pw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		}
	}
	public static void convertCreateTableSqlFromD2ToMSSQL(){
		//DB2 create table to MSSQL create table
		Map<String,String> map = FileUtils.findIdentityTables();
		File sqlFile = new File("C:/tmp/hc_sql.sql");
		File newSqlFile = new File("C:/tmp/hc_sql2.sql");
		BufferedReader br = null;
		PrintWriter pw = null;
		String line = null;
		String sqlType="MSSQL";
		
		try {
			String table = null;
			String column = null;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile)));
			pw = new PrintWriter(new FileOutputStream(newSqlFile));
			while((line = br.readLine()) != null){
				if(line.indexOf("CREATE TABLE") != -1){
					table = line.replace("CREATE TABLE", "").replace("\"DB2INST1\".", "").replace("\"", "").trim();
					if(map.get(table) != null){
						column = map.get(table);
					}
					line = "CREATE TABLE "  + table;
				}
				if(column != null && line.indexOf(column + " ") != -1){
					if(line.indexOf("bigint") != -1){
						line = line.replace(" bigint", " bigint IDENTITY(1,1) PRIMARY KEY");
					}
				}
				if(line.indexOf("CONSTRAINT") != -1){
					String repString = line.substring(line.indexOf("CONSTRAINT"),line.indexOf("PRIMARY KEY"));
					line = line.replace(repString, "");
				}
				if("MSSQL".equals(sqlType)){
					line = line.replace(" long varchar", " text");
					line = line.replace(" timestamp", " datetime");
				}
				if(line.indexOf(";") != -1){
					table = null;
					column = null;
				}
				pw.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pw != null){
				pw.close();
			}
		}
	}
	public static Map<String,String> findIdentityTables(){
		String[]path = new String[]{"C:/Users/ebti/workspace/hc/src/tw/com/ebti/web/hc/model/entity","C:/Users/ebti/workspace/hc/src/tw/com/ebti/web/model/entity"};
		Map<String,String> map = new HashMap<String,String>();
		for(String p : path){
			System.out.println("read path:" + p);
			File folder = new File(p);
			String[] files = folder.list();
			for(String f : files){
				System.out.println("read file:" + f);
				File item = new File(p+"/"+f);
				if(!item.isDirectory()){
					System.out.println("read file:" + f + " is file");
					BufferedReader br = null;
					try {
						br = new BufferedReader(new InputStreamReader(new FileInputStream(item)));
						String line = null;
						String table = null;
						String column = null;
						boolean readColumn = false;
						int i = 0;
						while((line = br.readLine()) != null){
							if(line.indexOf("@Table") != -1){
								table = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
								map.put(table, "{NOT FOUND}");
							}else if(line.indexOf("@GeneratedValue") != -1 && ( line.indexOf("SEQUENCE") != -1 || line.indexOf("IDENTITY") != -1 || line.indexOf("AUTO") != -1)){
								readColumn = true;
								i++;
							}else if(line.indexOf("@Column") != -1 && readColumn == true){
								column = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
								map.put(table, column);
								readColumn = false;
								continue;
							}else{
								//System.out.println("...-1");
							}
						}
						if(i == 0){
							map.remove(table);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(br != null){
							try {
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}else{
					System.out.println("read file:" + f + " is folder(skip)");
				}
				
			}
		}
		
		return map;
	}
	
	
	
	
	
	public static void changeExcel(){
		try {
			InputStream is = new FileInputStream(new File("C:/tmp/HealthCheck (1).xlsx"));
			Workbook inWorkbook = new XSSFWorkbook(is);	
			Sheet sheet = inWorkbook.getSheetAt(0);	
			
			
			for(int row=2 ; row<sheet.getLastRowNum()+1 ; row++){
				Row r = sheet.getRow(row);
				exportFile(r, row-1);
				 
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public static Map<Integer,String> map1 = new HashMap<Integer, String>();
	public static Map<Integer,String> map2 = new HashMap<Integer, String>();
	public static Map<Integer,String> map3 = new HashMap<Integer, String>();
	public static Map<Integer,String> map4 = new HashMap<Integer, String>();
	

	static{
		int i = 0;
		map1.put(i++,"1001");
		map1.put(i++,"1002");
		map1.put(i++,"1011");
		map1.put(i++,"2004");
		map1.put(i++,"2005");
		map1.put(i++,"2002");
		map1.put(i++,"2003");
		map1.put(i++,"1004");
		map1.put(i++,"1005");
		map1.put(i++,"1009");
		map1.put(i++,"1003");
		map1.put(i++,"2006");
		map1.put(i++,"2007");
		map1.put(i++,"2008");
		i = 0;
		map2.put(i++,"饭前血醣");	  
		map2.put(i++,"白血球");	  
		map2.put(i++,"血红素");		
		map2.put(i++,"收缩压");	  
		map2.put(i++,"舒张压");	  
		map2.put(i++,"身高");		  
		map2.put(i++,"体重");		  
		map2.put(i++,"三酸甘油脂");  
		map2.put(i++,"高密度胆固醇");
		map2.put(i++,"总胆固醇");   
		map2.put(i++,"尿酸");       
		map2.put(i++,"腰围");       
		map2.put(i++,"年龄");       
		map2.put(i++,"性别"); 
		i = 0;
		map3.put(i++,"血液中血糖浓度");	 
		map3.put(i++,"血液中白血球浓度");	 
		map3.put(i++,"");
		map3.put(i++,"单位mmHg");	 
		map3.put(i++,"单位mmHg");	 
		map3.put(i++,"身长多高,单位公分");	 
		map3.put(i++,"身体多重,单位公斤");	 
		map3.put(i++,"三酸甘油脂(TG)");	 
		map3.put(i++,"高密度胆固醇单位(mg/dl)");	 
		map3.put(i++,"总胆固醇(CHOL)");	 
		map3.put(i++,"尿酸碱值(PH)");	
		map3.put(i++,"公分");	 
		map3.put(i++,"");
		map3.put(i++,""); 
		
		
		
		
		i = 0;
		map4.put(i++, "xiexin");
		map4.put(i++, "wangxin");
		map4.put(i++, "liuyin");
		map4.put(i++, "wanglin");
		map4.put(i++, "xiaomin");

	}
	public static void exportFile(Row srcRow,int x){
		Workbook wb = new HSSFWorkbook();
	    Sheet sheet = wb.createSheet("new sheet");
    	int j = 0;
    	Row hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("項目ID");
    	hrow.createCell(1).setCellValue("健檢資料項目");
    	hrow.createCell(2).setCellValue("資料說明");
    	hrow.createCell(3).setCellValue("結果");
    	
    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("账号	必填 ");
    	hrow.createCell(2).setCellValue("健康云账号");
    	hrow.createCell(3).setCellValue(map4.get(x % 5));
    	DecimalFormat fmt = new DecimalFormat("00000000");
    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("健检编号");
    	hrow.createCell(2).setCellValue("必填");
    	hrow.createCell(3).setCellValue("MG"+fmt.format(x));

    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("健检年度");
    	hrow.createCell(2).setCellValue("必填  格式为yyyy");
    	hrow.createCell(3).setCellValue("2013");	

    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("健检日期");
    	hrow.createCell(2).setCellValue("必填  格式为yyyy/mm/dd");
    	hrow.createCell(3).setCellValue("2013-10-17");

    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("健检标准");
    	hrow.createCell(2).setCellValue("选填  格式为4码");
    	hrow.createCell(3).setCellValue("");
    		

    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("健检单位名称");
    	hrow.createCell(2).setCellValue("选填");
    	hrow.createCell(3).setCellValue("北京人民医院");
    		

    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("小区或企业专案");
    	hrow.createCell(2).setCellValue("选填  A：小区、B：企业");
    	hrow.createCell(3).setCellValue("");
    	
    	hrow = sheet.createRow((short)j++);
    	hrow.createCell(0).setCellValue("");
    	hrow.createCell(1).setCellValue("组织代码");
    	hrow.createCell(2).setCellValue("选填");
    	hrow.createCell(3).setCellValue("");
	    
	    
	    for(int i = 0;i < 14 ; i++){
	    	Row row = sheet.createRow((short)i + j);
	    	row.createCell(0).setCellValue(map1.get(i));
	    	row.createCell(1).setCellValue(map2.get(i));
	    	row.createCell(2).setCellValue(map3.get(i));
	    	try {
				row.createCell(3).setCellValue(srcRow.getCell(i).getStringCellValue());
			} catch (Exception e1) {
				row.createCell(3).setCellValue(srcRow.getCell(i).getNumericCellValue());
			}
	    	
	    	FileOutputStream fileOut = null;
		    try {
				fileOut = new FileOutputStream("C:/tmp/workbook_" +x+ ".xls");
				wb.write(fileOut);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fileOut != null){
					try {
						fileOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	    }
	}
}
