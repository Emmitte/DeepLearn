package script;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Text implements Comparable<Text>{
	
	public int i;
	public int loc;
	
	public Text(int i,int loc) {
		this.i = i;
		this.loc = loc;
	}

	public int compareTo(Text o) {
		if(this.i < o.i){
			return -1;
		}else if (this.i > o.i) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Text [i=" + i + ", loc=" + loc + "]";
	}
	
}

public class KFord_CV {
	
	public static FileReader fr = null;
	public static BufferedReader br = null;
	public static String line = null;
	public static FileOutputStream fos1 = null,fos2 = null;
	public static PrintStream ps1 = null,ps2 = null;
	public static List<Text> labelSet = new ArrayList<Text>();
	public static List<String> dataSet = new ArrayList<String>();
	public static List<Integer> lable_n_list = new ArrayList<Integer>();
	
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
	
	public static void readDataFile(String xPath,String yPath,double train_r,double test_r) {
		
		int loc = 0;
		try {  
            fr=new FileReader(yPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                Text t = new Text(Integer.parseInt(line), loc);
                labelSet.add(t);
                loc++;
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(labelSet);
		loc = 0;
		try {  
            fr=new FileReader(xPath);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null ) {  
                dataSet.add(line);
                loc++;
			}
            br.close();
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void fillLable_n_list() {
		int i,count,n;
		n = labelSet.size();
		count = 0;
		for(i = 0;i < n-1;i++){
			if(labelSet.get(i).i!=labelSet.get(i+1).i){
				count++;
				lable_n_list.add(count);
				count = 0;
			}else {
				count++;
			}
		}
		count++;
		lable_n_list.add(count);
	}
	
	public static void kFord(int i,int k) {
		List<Text> train = new ArrayList<Text>();
		List<Text> test = new ArrayList<Text>();
		int j,l,r,iter,n1,n2,sum;
		double count;
		n1 = labelSet.size();
		n2 = lable_n_list.size();
		sum = 0;
		for(j = 0;j < n2;j++){
			count = lable_n_list.get(j)*1.0/k;
			l = (int)count * i + sum;
			r = (int)(count) * (i+1) + sum;
			//System.out.println("count="+count+" l="+l+",r="+r);
			for(iter = sum;iter < sum+lable_n_list.get(j);iter++){
				if(iter >= l && iter < r){
					Text t = new Text(labelSet.get(iter).i, labelSet.get(iter).loc);
					test.add(t);
				}else {
					Text t = new Text(labelSet.get(iter).i, labelSet.get(iter).loc);
					train.add(t);
				}
			}
			sum += lable_n_list.get(j);
		}
		n1 = train.size();
		for(j = 0;j < n1;j++){
			ps1.println(train.get(j).i+"\t"+dataSet.get(train.get(j).loc));
		}
		n2 = test.size();
		for(j = 0;j < n2;j++){
			ps2.println(test.get(j).i+"\t"+dataSet.get(test.get(j).loc));
		}
	}
	
	public static void kFord(String dir,int k) {
		int i;
		String train,test;
		for(i = 0;i < k;i++){
			train = dir + "train" + (i+1) + ".txt";
			test = dir + "test" + (i+1) + ".txt";
			initWriter2(train, test);
			kFord(i, k);
			closeWriter2();
		}
	}

	public static void main(String[] args) {
		String dir,xPath,yPath,trainPath,testPath;
		double train_r,test_r;
		dir = "data/text/";
		xPath = dir + "work.txt";
		yPath = dir + "work_y.txt";
		trainPath = dir +"train.txt";
		testPath = dir + "test.txt";
		train_r = 0.6;
		test_r = 0.4;
		readDataFile(xPath, yPath, train_r, test_r);
		fillLable_n_list();
		dir = "data/text/kford";
		kFord(dir, 4);
	}

}
