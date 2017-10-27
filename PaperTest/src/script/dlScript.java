package script;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class dlScript {
	
	public static byte[] bt;
	public static InputStream is;
	public static Reader read;
	public static Lexeme t;
	public static IKSegmenter iks;
	public static FileReader fr=null;
	public static BufferedReader br=null;
	public static String line=null;
	public static FileOutputStream fos1 = null,fos2 = null;
	public static PrintStream ps1 = null,ps2 = null;
	
	/** 
	 * 初始化写文件器(单一指针)
	 * */
	public static void initWriter1(String writePath) {
		try {
			fos1 = new FileOutputStream(writePath);
			ps1 = new PrintStream(fos1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(单一指针)
	 * */
	public static void closeWriter1() {
		try {
			ps1.close();
			fos1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 初始化写文件器(双指针)
	 * */
	public static void initWriter2(String writePath1,String writePath2) {
		try {
			fos1 = new FileOutputStream(writePath1);
			ps1 = new PrintStream(fos1);
			fos2 = new FileOutputStream(writePath2);
			ps2 = new PrintStream(fos2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器(双指针)
	 * */
	public static void closeWriter2() {
		try {
			ps1.close();
			fos1.close();
			ps2.close();
			fos2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * input:readPath1,readPath2,writePath
	 * 合并readPath1与readPath2的文件到writePath文件中
	 * */
	public static void mergeFile(String readPath1,String readPath2,String writePath) {
		FileReader fr1=null;
		BufferedReader br1=null;
		String line1=null;
		FileReader fr2=null;
		BufferedReader br2=null;
		String line2=null;
		FileOutputStream fos=null;
		PrintStream ps=null;
		try {  
            fr1=new FileReader(readPath1);
			br1=new BufferedReader(fr1);
			fr2=new FileReader(readPath2);
			br2=new BufferedReader(fr2);
			fos=new FileOutputStream(writePath);
			ps=new PrintStream(fos);
            while ((line1=br1.readLine()) != null && (line2=br2.readLine()) != null) {  
                ps.println(line2 + "\t" + line1);
			}
            ps.close();
            fos.close();
            br1.close();
            br2.close();
            fr1.close();
            fr2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 * input:readPath,writePath
	 * 放大数据的数值，放大100倍
	 * */
	public static void enlargeNum(String readPath) {
		
		int y;
		double x;
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreElements()){
                	x = Double.parseDouble(st.nextToken());
                	y = 0;
                	if(x > 0){
                		y = (int)(x * 100);
                	}
                	ps1.print(y + "\t");
                }
                ps1.println();
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 * input:readPath,writePath,num,col
	 * 根据指定的col列，放大数据的数值，放大num倍
	 * */
	public static void enlargeNum(String readPath,int num,int col) {

		int i = 0,y;
		double x;
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                StringTokenizer st = new StringTokenizer(line);
                i = 0;
                while(st.hasMoreElements()){
                	x = Double.parseDouble(st.nextToken());
                	if(i == col){
                		y = (int)(x + num);
                	}else {
                		y = (int)x;
					}
                	ps1.print(y + "\t");
                	i++;
                }
                ps1.println();
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:str
	 * output:String[]
	 * 读取dir目录下的全部文件名，返回到字符数组中
	 */
	public static String[] readDir(String dir) {
		File dirFile = new File(dir);
		String[] list = null;
		if(dirFile.isDirectory()){
			 list = dirFile.list();
		}
		return list;
	}
	
	/**
	 * input:json串
	 * 解析jons字符串，只提取Text和Label的信息
	 */
	public static String[] jsonToString(String key_json) {

		JSONObject jsonContent = JSON.parseObject(key_json);
		String text = jsonContent.getString("Text");
		String category = jsonContent.getString("Label");
		String[] strArray = new String[2];
		strArray[0] = text;
		strArray[1] = category;
		return strArray;
	}
	/**
	 * input:readPath
	 * 将含有json串的文件进行解析，将解析后的文件分别写入到训练集文件和测试集文件中
	 */
	public static void writeFile(String readPath) {
		
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
			
            while ((line=br.readLine()) != null ) {  
                String[] strArray = jsonToString(line);
                ps1.println(strArray[0]);
                ps2.println(strArray[1]);
			}
            
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void makeDic(String path) {
		Set<String> dic = new HashSet<String>();
		try {  
            fr=new FileReader(path);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
            	bt = line.getBytes();
				is = new ByteArrayInputStream(bt);
				read = new InputStreamReader(is);
				iks = new IKSegmenter(read, true);
				while ((t = iks.next()) != null) {
					dic.add(t.getLexemeText());
				}
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator<String> iter = dic.iterator();
		while(iter.hasNext()){
			ps1.println(iter.next());
		}
	}
	
	/**
	 * input:path
	 * output:List<String>
	 * 读取词典信息，以list返回词典
	 */
	public static List<String> readDic(String path) {
		List<String> list = new ArrayList<String>();
		try {  
            fr=new FileReader(path);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreElements()){
                	list.add(st.nextToken());
                }
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * input:path1,path2,writePath
	 * 删除停用词，通过读取path2中的停用词表，将path1中的停用词删除，生成的新文件写入到writePath中
	 */
	public static void deleteStopWords(String path1, String path2, String writePath) {

		List<String> trainDic = readDic(path1);
		List<String> stopDic = readDic(path2);
		int i,n;
		n = stopDic.size();
		try {  
			for(i = 0;i < n;i++){
				if(trainDic.contains(stopDic.get(i))){
					trainDic.remove(stopDic.get(i));
				}
			}
			n = trainDic.size();
			for(i = 0;i < n;i++){
				ps1.println(trainDic.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:path
	 * 删除停用词，将文件中含有的数字、字母的单词删除
	 */
	public static void deleteStopWords(String path) {
		List<String> trainDic = readDic(path);
		List<String> list = new ArrayList<String>();
		int i,j,n,m;
		n = trainDic.size();
		System.out.println(n);
		for(i = 0;i < n;i++){
			m = trainDic.get(i).length();
			StringBuffer sb = new StringBuffer();
			for(j = 0;j < m;j++){
				if((trainDic.get(i).charAt(j) >= 'A' && trainDic.get(i).charAt(j) <= 'Z')){
					
				}else if (trainDic.get(i).charAt(j) >= 'a' && trainDic.get(i).charAt(j) <= 'z') {
					
				}else {
					sb.append(trainDic.get(i).charAt(j));
				}
			}
			list.add(sb.toString());
		}
		n = list.size();
		for(i = 0;i < n;i++){
			ps1.println(list.get(i));
		}
	}
	
	public static void read(String readpath) {
		try {  
            fr=new FileReader(readpath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                ps1.println(line);
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		/*
		String dir = "data/SA/text/2/";
		String readPath = dir + "test_y.txt";
		String writePath = "data/SA/text/2/test_y1.txt";
		enlargeNum(readPath, writePath,-1,0);
		*/
		/*
		String dir = "data/SA/3/";
		String readPath1 = dir + "test_x.txt";
		String readPath2 = dir + "test_y.txt";
		String writePath = "data/dl/11/test.data";
		mergeFile(readPath1, readPath2, writePath);
		*/
		/*
		String dir = "data/SA/text/test/fold_4/";
		String[] paths = readDir(dir);
		String xPath = "data/SA/text/test4_x.txt";
		String yPath = "data/SA/text/test4_y.txt";
		initWriter(xPath, yPath);
		for(int i = 0;i < paths.length;i++){
			writeFile(dir + paths[i]);
		}
		closeWriter();
		*/
		/*
		String dic = args[0] +  "/";
		String trainDic = dic + "dic.txt";
		String stopDic = dic + "stopword.dic";
		String writePath = dic + "newDic2.txt";
		//deleteStopWords(trainDic, stopDic, writePath);
		//deleteStopWords(writePath);
		System.out.println(readDic(writePath).size());
		*/
		/*
		String dir = "data/abstract/";
		String readPath = dir + "train_x.txt";
		String stopPath = dir + "stopword.dic";
		String writePath1 = dir + "dic1.txt";
		initWriter1(writePath1);
		makeDic(readPath);
		closeWriter1();
		String writePath2 = dir + "dic2.txt";
		initWriter1(writePath2);
		deleteStopWords(writePath1);
		closeWriter1();
		String writePath3 = dir + "dic3.txt";
		initWriter1(writePath3);
		deleteStopWords(writePath2, stopPath, writePath3);
		closeWriter1();
		*/
		String dir = "data/";
		String readPath = dir + "a.docx";
		String writePath = dir + "new.docx";
		initWriter1(writePath);
		read(readPath);
		closeWriter1();
	}

}
