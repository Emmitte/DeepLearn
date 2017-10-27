package script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import algorithm.LogisticRegression;

public class stockTest1 {
	
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
	
	public static void process() {
		String dir = "data/stock/stockdata6/";
		String trainPath = dir + "dataTrain.data";
		String testPath = dir + "dataTest.data";
		List<List<Double>> trainData = null,testData = null;
		trainData = read(trainPath);
		testData = read(testPath);

		double learning_rate = 0.1;
        int n_epochs = 5000;

        int train_N = trainData.size();
        int test_N = testData.size();
        int n_in = 8;
        int n_out = 3;
        
        double[] train_X = null;
        int[] train_Y = null;
        double[] test_X = null;
        double[] test_Y = null;
        double temp = 0.0;
        
        int count=0;

		LogisticRegression classifier = new LogisticRegression(train_N, n_in,
				n_out);

		// train
		for (int epoch = 0; epoch < n_epochs; epoch++) {
			for (int i = 0; i < train_N; i++) {
				train_X = new double[n_in];
				train_Y = new int[n_out];
				//System.out.println(trainData.get(i));
				for(int j = 0;j < n_in;j++){
					train_X[j] = trainData.get(i).get(j);
					//System.out.print(train_X[j]+" ");
				}
				//System.out.println();System.exit(0);
				for(int j = 0;j < n_out;j++){
					temp = trainData.get(i).get(trainData.get(i).size()-n_out+j);
					train_Y[j] = (int) temp;
				}
				classifier.train(train_X, train_Y, learning_rate);
			}
			// learning_rate *= 0.95;
		}
		
		// test
        for(int i=0; i<test_N; i++) {
        	test_X = new double[n_in];
        	test_Y = new double[n_out];
        	for(int j = 0;j < n_in;j++){
        		test_X[j] = testData.get(i).get(j);
        	}
        	
            classifier.predict(test_X, test_Y);
            for(int j=0; j<n_out; j++) {
                //System.out.print(test_Y[j] + " ");
            	
            	if(test_Y[j] > 0.5){
            		System.out.print(1 + " ");
            	}else {
            		System.out.print(0 + " ");
				}
            	if(test_Y[j] > 0.5 && testData.get(i).get(testData.get(i).size()-n_out+j) > 0.0){
            		count++;
            		//System.out.println("ret="+testData.get(i).get(testData.get(i).size()-n_out+j));
            	}
            }
            System.out.println();
        }
        System.out.println(test_N);
        System.out.println("accurate:" + count*1.0/test_N*100 + "%");
	}
	
	public static void main(String[] args) {
		process();
	}

}
