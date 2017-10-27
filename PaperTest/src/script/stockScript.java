package script;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class stockScript {
	
	public static List<List<Double>> read(String path) {
		List<List<Double>> dataSet = new ArrayList<List<Double>>();
		FileReader fr=null;
		BufferedReader br=null;
		String line=null;
        List<Double> data = null; 
        try {  
            fr=new FileReader(path);
			br=new BufferedReader(fr);
            while ((line=br.readLine()) != null) {  
                String strArray[] = line.split("\t");  
                data = new ArrayList<Double>();
                for (int i = 0; i < strArray.length; i++) {
                	if(strArray[i].contains("%")){
                		data.add(Double.parseDouble(strArray[i].substring(0, strArray[i].length()-1)));
                	}else {
                		data.add(Double.parseDouble(strArray[i]));
					}
                }  
                dataSet.add(data);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return dataSet;
	}
	
	public static void makeFile(String readPath, String writePath) {
		FileReader fr=null;
		BufferedReader br=null;
		FileOutputStream fos=null;
		PrintStream ps=null;
		String line=null;
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		try {  
            fr=new FileReader(readPath);
			br=new BufferedReader(fr);
			fos=new FileOutputStream(writePath);
			ps=new PrintStream(fos);
            while ((line=br.readLine()) != null) {
            	double x = 0.0;
            	if(line.contains("-")){
            		x = -1 * Double.parseDouble(line.substring(1, line.length()-1));
            	}else {
					x = Double.parseDouble(line.substring(0, line.length()-1)); 
				}
                if(x > 0.05){
                	ps.println(1 + "\t" + 0 + "\t" + 0);
                	count1++;
                }else if (x < -0.05) {
                	ps.println(0 + "\t" + 0 + "\t" + 1);
					count3++;
				}else {
					ps.println(0 + "\t" + 1 + "\t" + 0);
					count2++;
				}
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		System.out.println("c1="+count1+" c2="+count2+" c3="+count3);
	}
	
	public static void makeVolume(List<List<Double>> dataSet, String writePath) {
		
		FileOutputStream fos=null;
		PrintStream ps=null;
		
		try {  
			fos=new FileOutputStream(writePath);
			ps=new PrintStream(fos);
            for(int i = 0;i < dataSet.size()-1;i++){
            	//System.out.println(dataSet.get(i));
            	//System.out.println(dataSet.get(i).get(6) + " " + dataSet.get(i).get(7) + "\t" + dataSet.get(i+1).get(6) + " " + dataSet.get(i+1).get(7));
            	ps.println((dataSet.get(i+1).get(6)-dataSet.get(i).get(6)) + "\t" + (dataSet.get(i+1).get(7)-dataSet.get(i).get(7)));
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public static void makeNormalization1(List<List<Double>> dataSet, String writePath, int col) {

		FileOutputStream fos=null;
		PrintStream ps=null;
		double min = 0.0,max = 0.0,temp = 0.0;
		
		try {  
			fos=new FileOutputStream(writePath);
			ps=new PrintStream(fos);
            for(int i = 0;i < dataSet.size();i++){
            	if(dataSet.get(i).get(col) < min){
            		min = dataSet.get(i).get(col);
            	}
            	if(dataSet.get(i).get(col) > max){
            		max = dataSet.get(i).get(col);
            	}
            }
            System.out.println("max="+max+" min="+min);
            for(int i = 0;i < dataSet.size();i++){
            	temp = (dataSet.get(i).get(col) - min) / (max - min);
            	ps.println(temp);
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public static void makeNormalization2(List<List<Double>> dataSet, String writePath, int col) {

		FileOutputStream fos=null;
		PrintStream ps=null;
		double min = 0.0,max = 0.0,temp = 0.0;
		
		try {  
			fos=new FileOutputStream(writePath);
			ps=new PrintStream(fos);
            for(int i = 0;i < dataSet.size();i++){
            	if(dataSet.get(i).get(col) < min){
            		min = dataSet.get(i).get(col);
            	}
            	if(dataSet.get(i).get(col) > max){
            		max = dataSet.get(i).get(col);
            	}
            }
            System.out.println("max="+max+" min="+min);
            for(int i = 0;i < dataSet.size();i++){
            	if(dataSet.get(i).get(col) < 0){
            		temp = -1 * dataSet.get(i).get(col) / min;
            	}else {
					temp = dataSet.get(i).get(col) / max;
				}
            	ps.println(temp);
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public static void process() {
		String dir = "data/stock/";
		String readPath1 = dir + "data_X2.data";
		String readPath2 = dir + "data.data";
		String writePath = dir + "output.data";
		List<List<Double>> dataSet = read(readPath1); 
		makeNormalization2(dataSet, writePath, 7);
	}
	
	public static void main(String[] args) {
		process();
	}

}
