package script;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

class T implements Comparable<T>{
	public String name;
	public double score;
	public T(String name, double score) {
		this.name = name;
		this.score = score;
	}
	@Override
	public String toString() {
		return name + "\t" + score;
	}
	public int compareTo(T o) {
		if(this.score < o.score){
			return 1;
		}else if (this.score > o.score) {
			return -1;
		}else {
			return 0;
		}
	}
	
}

public class spliteWords {

	public static byte[] bt;
	public static InputStream is;
	public static Reader read;
	public static Lexeme t;
	public static IKSegmenter iks;
	public static FileInputStream in;
	public static FileReader fr = null;
	public static BufferedReader br = null;
	public static String line = null;
	public static FileOutputStream fos = null;
	public static PrintStream ps = null;
	
	public static List<String> dic = new ArrayList<String>();

	public static void test(String str) {
		bt = str.getBytes();
		is = new ByteArrayInputStream(bt);
		read = new InputStreamReader(is);
		iks = new IKSegmenter(read, true);
		try {
			while ((t = iks.next()) != null) {
				System.out.print(t.getLexemeText() + " ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:str
	 * output:String
	 * 匹配字符串的编码格式
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 匹配GB2312
				String s = encode;
				return s; 
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 匹配ISO-8859-1
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 匹配UTF-8
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) { // 匹配GBK
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return ""; // 返回字符编码格式
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
	 * 初始化写文件器
	*/
	public static void initWriter(String writePath) {
		try {
			fos = new FileOutputStream(writePath);
			ps = new PrintStream(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/** 
	 * 关闭文件器
	 * */
	public static void closeWriter() {
		try {
			ps.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:readPath
	 * 读取readPath文件的内容，重定向到另一个文件
	 */
	public static void copyFile(String readPath) {
		
		try {
			fr = new FileReader(readPath);
			br = new BufferedReader(fr);
			System.out.println(getEncoding(line));
			while ((line = br.readLine()) != null) {
				System.out.println(getEncoding(line));
				System.out.println(line);
				ps.println(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
                	String str = st.nextToken();
                	//if(!list.contains(str)){
                		list.add(str);
                	//}
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
	 * input:readPath
	 * output:List<List<String>>
	 * 读取x数据，分词，生成按词向量生成向量矩阵
	 */
	public static List<List<String>> readXFile(String readPath) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> data;
		int count = 0,line_count = 0;
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
            	if(line_count == 400){
            		break;
            	}
            	data = new ArrayList<String>();
            	bt = line.getBytes();
				is = new ByteArrayInputStream(bt);
				read = new InputStreamReader(is);
				iks = new IKSegmenter(read, true);
				while ((t = iks.next()) != null) {
					data.add(t.getLexemeText());
	                count++;
				}
				list.add(data);
				line_count++;
			}
            System.out.println(count);
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * input:readPath,dicPath
	 * 读取readPath文件，分词，按dicPath的词向量生成向量矩阵，写入到新文件中
	 */
	public static void process2(String readPath, String dicPath) {

		List<String> dimList = readDic(dicPath);
		int n = dimList.size();
		System.out.println(n);
		try {
			fr = new FileReader(readPath);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				int[] dimArray = new int[n];
				bt = line.getBytes();
				is = new ByteArrayInputStream(bt);
				read = new InputStreamReader(is);
				iks = new IKSegmenter(read, true);
				while ((t = iks.next()) != null) {
					if(dimList.contains(t.getLexemeText())){
						dimArray[dimList.indexOf(t.getLexemeText())]++;
					}
				}
				for(int i = 0;i < n;i++){
					ps.print(dimArray[i]+"\t");
				}
				ps.println();
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:readPath
	 * 读取readPath文件，分词，并将含有数字、字母的字符串过滤掉，生成向量矩阵，写入到新文件中
	 */
	public static void process3(String readPath) {
		List<String> list = new ArrayList<String>();
		int count = 0,n = 0;
		try {
			fr = new FileReader(readPath);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				/*
				if(count == 400){
					break;
				}
				*/
				bt = line.getBytes();
				is = new ByteArrayInputStream(bt);
				read = new InputStreamReader(is);
				iks = new IKSegmenter(read, true);
				while ((t = iks.next()) != null) {
					if(t.getLexemeText().toLowerCase().charAt(0) >= 'a' && t.getLexemeText().toLowerCase().charAt(0) <= 'z'){
						continue;
					}else if (t.getLexemeText().toLowerCase().charAt(0) >= '0' && t.getLexemeText().toLowerCase().charAt(0) <= '9') {
						continue;
					}else {
						list.add(t.getLexemeText());
					}
					count++;
				}
			}
			System.out.println(count);
			n = list.size();
			for(int i = 0;i < n;i++){
				ps.println(list.get(i));
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * input:path
	 * 统计文章单词总数（不做重复处理）
	 */
	public static int wordsCount(String path) {
		int count = 0;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			while((line = br.readLine()) != null){
				bt = line.getBytes();
				is = new ByteArrayInputStream(bt);
				read = new InputStreamReader(is);
				iks = new IKSegmenter(read, true);
				while ((t = iks.next()) != null) {
					if(t.getLexemeText().toLowerCase().charAt(0) >= 'a' && t.getLexemeText().toLowerCase().charAt(0) <= 'z'){
						continue;
					}else if (t.getLexemeText().toLowerCase().charAt(0) >= '0' && t.getLexemeText().toLowerCase().charAt(0) <= '9') {
						continue;
					}else {
						count++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * input:dicPath,path,n_words(不能做重复归并)
	 * 计算ti*idf
	 */
	public static void calTf_Idf(String dicPath,String path,int n_words) {
		List<String> dicList = readDic(dicPath);
		List<List<String>> xList = readXFile(path);
		int i,j,k,n_dic,n_docs,count_tf,count_idf;
		n_dic = dicList.size();
		n_docs = xList.size();
		List<T> list = new ArrayList<T>();
		for(i = 0;i < n_dic;i++){
			count_tf = 0;
			count_idf = 0;
			for(j = 0;j < n_docs;j++){
				if(xList.get(j).contains(dicList.get(i))){
					for(k = 0;k < xList.get(j).size();k++){
						if(xList.get(j).get(k).equals(dicList.get(i))){
							count_tf++;
						}
					}
					count_idf++;
				}
			}
			double score = 0.0,tf= 0.0,idf = 0.0;
			tf = (count_tf * 1.0) / n_words;
			idf = Math.log((n_docs * 1.0+1) / (count_idf+1));
			score = tf * idf;
			//System.out.println("tf_c="+count_tf+" word="+n_words+" tf="+tf+",idf_c="+count_idf+" docs="+n_docs+" idf="+idf+",score="+score);
			//System.exit(0);
			T t = new T(dicList.get(i), score);
			list.add(t);
		}
		Collections.sort(list);
		n_docs = list.size();
		for(i = 0;i < n_docs;i++){
			ps.println(list.get(i).name);
		}
	}
	/**
	 * input:dicPath,readXPath,readYPath
	 * 计算卡方检验(CHI)
	 */
	public static void calCHI(String dicPath, String readXPath, String readYPath) {
		List<String> dicList = readDic(dicPath);
		List<List<String>> xList = readXFile(readXPath);
		List<String> yList = readDic(readYPath);
		List<T> list = new ArrayList<T>();
		int i,j,k,n_dic,n_docs,a,b,c,d;
		n_dic = dicList.size();
		n_docs = xList.size();
		for(i = 0;i < n_dic;i++){
			double[] chi = new double[3];
			for(k = 0;k < 3;k++){
				a = 0;
				b = 0;
				c = 0;
				d = 0;
				for(j = 0; j < n_docs;j++){
					if(xList.get(j).contains(dicList.get(i))){
						if(Integer.parseInt(yList.get(j)) == k){
							a++;
						}else {
							b++;
						}
					}else {
						if(Integer.parseInt(yList.get(j)) == k){
							c++;
						}else {
							d++;
						}
					}
				}
				double temp = 0.0;
				temp = (1.0 * (n_docs * 1.0 * Math.pow((a*d - b*c), 2)) + 1) / ((a + c)*(b + d)*(a + b)*(c + d) + 1);
				chi[k] = temp;
			}
			double score = chi[0];
			for(k = 1;k < 3;k++){
				if(score < chi[k]){
					score = chi[k];
				}
			}
			T t = new T(dicList.get(i), score);
			list.add(t);
		}
		Collections.sort(list);
		n_docs = list.size();
		for(i = 0;i < n_docs;i++){
			ps.println(list.get(i).name+" ");
		}
	}
	/**
	 * input:dicPath,readYPath
	 * 计算信息熵(HC)
	 */
	public static double getHC(String dicPath, String readYPath) {
		List<String> yList = readDic(readYPath);
		int i,j,n,count;
		double sum,temp,p;
		count = 0;
		sum = 0;
		temp = 0;
		n = yList.size();
		for(i = 0;i < 3;i++){
			for(j = 0;j < n;j++){
				if(Integer.parseInt(yList.get(j)) == i){
					count++;
				}
			}
			p = count * 1.0 / n;
			temp = p * Math.log(p);
			sum -= temp;
		}
		return sum;
	}
	/**
	 * input:dicPath,readXPath,readYPath,n_words,HC
	 * 计算信息增益(IG)
	 */
	public static void calIG(String dicPath, String readXPath, String readYPath, int n_words,double HC) {
		List<String> dicList = readDic(dicPath);
		List<List<String>> xList = readXFile(readXPath);
		List<String> yList = readDic(readYPath);
		List<T> list = new ArrayList<T>();
		int i,j,k,n_dic,n_docs,count_t1,count_t2,count_ct1,count_ct2;
		double hct,pt1 = 0,pt2 = 0,pct1,pct2;
		n_dic = dicList.size();
		n_docs = xList.size();
		for(i = 0;i < n_dic;i++){
			hct = 0.0;
			pct1 = 0.0;
			pct2 = 0.0;
			for(k = 0;k < 3;k++){
				pt1 = 0.0;
				pt2 = 0.0;
				count_t1 = 0;
				count_t2 = 0;
				count_ct1 = 0;
				count_ct2 = 0;
				for(j = 0; j < n_docs;j++){
					if(xList.get(j).contains(dicList.get(i))){
						count_t1++;
						if(Integer.parseInt(yList.get(j)) == k){
							count_ct1++;
						}
					}else {
						count_t2++;
						if(Integer.parseInt(yList.get(j)) == k){
							count_ct2++;
						}
					}
				}
				pt1 = count_t1 * 1.0 / n_docs;
				pt2 = count_t2 * 1.0 / n_docs;
				double temp1,temp2;
				if(count_ct1 == 0){
					//System.out.println(dicList.get(i) + "t1="+count_t1+" ct1=" + count_ct1+" t2="+count_t2+" ct2="+count_ct2);
					temp1 = (count_ct1 * 1.0 / count_t1) * (Math.log(0.0001));
					temp2 = (count_ct2 * 1.0 / count_t2) * (Math.log(count_ct2 * 1.0 / count_t2));
				}else if (count_ct2 == 0) {
					temp1 = (count_ct1 * 1.0 / count_t1) * (Math.log(count_ct1 * 1.0 / count_t1));
					temp2 = (count_ct2 * 1.0 / count_t2) * (Math.log(0.0001));
				}else {
					temp1 = (count_ct1 * 1.0 / count_t1) * (Math.log(count_ct1 * 1.0 / count_t1));
					temp2 = (count_ct2 * 1.0 / count_t2) * (Math.log(count_ct2 * 1.0 / count_t2));
				}
				pct1 += temp1;
				pct2 += temp2;
			}
			hct = pt1 * pct1 + pt2 * pct2;
			double score = 0.0;
			score = HC + hct;
			T t = new T(dicList.get(i), score);
			list.add(t);
		}
		Collections.sort(list);
		n_docs = list.size();
		for(i = 0;i < n_docs;i++){
			ps.println(list.get(i).name);
		}
	}
	/**
	 * input:path1,path2,path3
	 * 选择文件内容
	 */
	public static void selectFile(String path1, String path2,int n) {
		List<String> list1 = readDic(path1);
		List<String> list2 = readDic(path2);
		int i,j;
		if(n > list1.size()){
			n = list1.size();
		}
		for(i = 0;i < n;i++){
			for(j = 0;j < n;j++){
				if(list1.get(i).equals(list2.get(j))){
					ps.println(list1.get(i));
					break;
				}
			}
		}
		
	}
	/**
	 * input:readPath
	 * 过滤重复数据
	 */
	public static void filter(String readPath) {
		List<String> list = new ArrayList<String>();
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreElements()){
                	String str = st.nextToken();
                	if(!list.contains(str)){
                		list.add(str);
                	}
                }
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int n = list.size();
		for(int i = 0;i < n;i++){
			ps.println(list.get(i));
		}
	}
	/**
	 * input:path
	 * output:List<Integer>
	 * 获取标签(Y)数据,以list形式返回
	 */
	public static List<Integer> getYList(String path) {
		List<Integer> list = new ArrayList<Integer>();
		int count = 0;
		try {  
            fr=new FileReader(path);
			br=new BufferedReader(fr);
			
            while ((line=br.readLine()) != null ) {  
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreElements()){
                	String str = st.nextToken();
                	list.add(Integer.parseInt(str));
                	count++;
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
	 * input:Ypath,Xpath
	 * 计算召回率、准确率、F-测度值
	 */
	public static void getResult1(String Ypath,String Xpath) {
		double r,r0,r1,r2,p0,p1,p2,f0,f1,f2,max;
		r = 0.0;
		int P0,P1,P2,TP0,TP1,TP2,PN0,PN1,PN2,n,i,flag,count1,count2,loc;
		List<Integer> yList = getYList(Ypath);
		n = yList.size();
		FileReader fr=null;
		BufferedReader br=null;
		String line=null;
		flag = 1;
		P0 = 0;
		P1 = 0;
		P2 = 0;
		TP0 = 0;
		TP1 = 0;
		TP2 = 0;
		PN0 = 0;
		PN1 = 0;
		PN2 = 0;
		for(i = 0;i < n;i++){
			if(yList.get(i) == 0){
				P0++;
			}
			if(yList.get(i) == 1){
				P1++;
			}
			if(yList.get(i) == 2){
				P2++;
			}
		}
		
		try {  
            fr=new FileReader(Xpath);
			br=new BufferedReader(fr);
			i = 0;
			count1 = 0;
            while ((line=br.readLine()) != null ) { 
            	if(count1 == 200){
            		break;
            	}
                if(flag % 2 == 0){
                	System.out.println(line);
                    max = 0.0;
                    loc = 0;
                    count2 = 0;
                    StringTokenizer st = new StringTokenizer(line);
                    while(st.hasMoreElements()){
                    	if(count2 == 3){
                    		break;
                    	}
                    	double temp = Double.parseDouble(st.nextToken());
                    	if(max < temp){
                    		max = temp;
                    		loc = count2;
                    	}
                    	count2++;
                    }
                    if(loc == 0){
                    	PN0++;
                    }
                    if(loc == 1){
                    	PN1++;
                    }
                    if(loc == 2){
                    	PN2++;
                    }
                    if(loc == yList.get(i) && loc == 0){
                    	TP0++;
                    }
                    if(loc == yList.get(i) && loc == 1){
                    	TP1++;
                    }
                    if(loc == yList.get(i) && loc == 2){
                    	TP2++;
                    }
                    i++;
                    count1++;
                    System.out.println(flag);
            	}
                flag++;
			}
            
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("TO0="+TP0+" P0="+P0+" PN0="+PN0+" TP1="+TP1+" P1="+P1+" PN1="+PN1+" TP2="+TP2+" P2="+P2+" PN2="+PN2);
		r0 = (TP0*1.0/P0);
		r1 = (TP1*1.0/P1);
		r2 = (TP2*1.0/P2);
		System.out.println("r0="+r0+"\tr1="+r1+"\tr2="+r2);
		p0 = (TP0*1.0/PN0);
		p1 = (TP1*1.0/PN1);
		p2 = (TP2*1.0/PN2);
		System.out.println("p0="+p0+"\tp1="+p1+"\tp2="+p2);
		f0 = (2*p0*r0)/(p0+r0);
		f1 = (2*p1*r1)/(p1+r1);
		f2 = (2*p2*r2)/(p2+r2);
		System.out.println("f0="+f0+"\tf1="+f1+"\tf2="+f2);
	}
	/**
	 * input:Ypath,Xpath,n_c(类别的个数)
	 * 计算召回率、准确率、F-测度值
	 */
	public static void getResult(String Ypath,String Xpath,int n_c) {
		double max;
		int n,i,flag,count1,count2,loc;
		int[] P = new int[n_c];
		int[] TP = new int[n_c];
		int[] PN = new int[n_c];
		List<Integer> yList = getYList(Ypath);
		n = yList.size();
		FileReader fr=null;
		BufferedReader br=null;
		String line=null;
		for(i = 0;i < n_c;i++){
			P[i] = 0;
			TP[i] = 0;
			PN[i] = 0;
		}
		flag = 1;
		for(i = 0;i < n;i++){
			P[yList.get(i)]++;
		}
		
		try {  
            fr=new FileReader(Xpath);
			br=new BufferedReader(fr);
			count1 = 0;
            while ((line=br.readLine()) != null ) { 
            	if(count1 == n){
            		break;
            	}
                if(flag % 2 == 0){
                	//System.out.println(line);
                    max = 0.0;
                    loc = 0;
                    count2 = 0;
                    StringTokenizer st = new StringTokenizer(line);
                    while(st.hasMoreElements()){
                    	if(count2 == n_c){
                    		break;
                    	}
                    	double temp = Double.parseDouble(st.nextToken());
                    	if(max < temp){
                    		max = temp;
                    		loc = count2;
                    	}
                    	count2++;
                    }
                    PN[loc]++;
                    if(loc == yList.get(count1)){
                    	TP[loc]++;
                    }
                    count1++;
            	}
                flag++;
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		double[] r = new double[n_c];
		double[] p = new double[n_c];
		double[] f = new double[n_c];
		for(i = 0;i < n_c;i++){
			r[i] = TP[i]*1.0/P[i];
			p[i] = TP[i]*1.0/PN[i];
			f[i] = (2*p[i]*r[i])/(p[i]+r[i]);
			System.out.println(i+":");
			System.out.println("TP"+i+"="+TP[i]+" P"+i+"="+P[i]+" PN="+i+PN[i]);
			System.out.println("r"+i+"="+r[i]+"\tp"+i+"="+p[i]+"\tf"+i+"="+f[i]);
		}
	}

	public static void main(String[] args) {
		/*
		//String dir = "data/SA/COAE2014dataset/";
		String dir = "data/SA/dataset/";
		String[] list = readDir(dir);
		String readPath;
		String writePath = "data/SA/out3.txt";
		int i,n;
		n = list.length;
		initWriter(writePath);
		for(i = 0;i < n;i++){
			readPath = dir + list[i];
			process2(readPath);
		}
		closeWriter();
		*/
		/*
		String dir = "data/SA/text/1/train/";
		//String dir = args[0] + "/";
		//String readPath = dir + "out.txt";
		String readPath = dir + "train_x.txt";
		String dicPath = dir + "newDic2.txt";
		String writePath = dir + "train_x.data";
		//String readPath = dir + args[1];
		//String writePath = dir + args[2];
		initWriter(writePath);
		process2(readPath,dicPath);
		closeWriter();
		*/
		/*
		String dir = "data/SA/text/1/train/";
		String readXPath = dir + "train_x_new.txt";
		String readYPath = dir + "train_y_new.txt";
		String dicPath = dir + "temp_dic.txt";
		String path1 = dir + "temp_dic.txt";
		String path2 = dir + "dic6-bad.txt";
		String writePath1 = dir + "temp_bad.txt";
		String writePath2 = dir + "temp.txt";
		*/
		//initWriter(writePath1);
		//filter(dicPath);
		//calTf_Idf(dicPath, readXPath, 6240);
		//calCHI(dicPath, readXPath, readYPath);
		//calIG(dicPath, readXPath, readYPath, 19814, getHC(dicPath, readYPath));
		//selectFile(path1, path2,writePath2);
		//closeWriter();
		
		String dir = "data/abstract/2/";
		String readYPath = dir + "test_y.txt";
		String readXPath = dir + "log-softmax-2";
		getResult(readYPath, readXPath,7);
		
		/*
		String dir = "data/abstract/";
		String readXPath = dir + "train_x.txt";
		String readYPath = dir + "train_y.txt";
		String dicPath = dir + "dic3.txt";
		String writePath1 = dir + "dic4.txt";
		String writePath2 = dir + "dic5.txt";
		
		initWriter(writePath1);
		int n = wordsCount(readXPath);
		calTf_Idf(dicPath, readXPath, n);
		closeWriter();
		
		
		initWriter(writePath2);
		calCHI(dicPath, readXPath, readYPath);
		closeWriter();
		
		String writePath3 = dir + "dic6.txt";
		initWriter(writePath3);
		selectFile(writePath1, writePath2, 3000);
		closeWriter();
		*/
	}

}
