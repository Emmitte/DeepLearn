package script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import algorithm.SdA;
import algorithm.SdA2;
import algorithm.SdA3;

public class sdaeScript {

	/**
	 * input:path,m,n
	 * output:int[n][m]
	 * 读取数据(x)文件,生成一个n行m列的矩阵
	 */
	public static int[][] read_x(String path, int m, int n) {
		int[][] dataset = new int[n][m];
		FileReader fr = null;
		BufferedReader br = null;
		String line = null;
		int i = 0;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				StringTokenizer st = new StringTokenizer(line);
				for (int j = 0; j < m; j++) {
					dataset[i][j] = Integer.parseInt(st.nextToken());
					//System.out.println(dataset[i][j]);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}

	/**
	 * input:path,m,n
	 * output:int[n][m]
	 * 读取标签(y)文件,生成一个n行m列的矩阵,比如:标签为0->100,1->010,2->001
	 */
	public static int[][] read_y(String path, int m, int n) {
		FileReader fr = null;
		BufferedReader br = null;
		String line = null;
		int[][] dataset = new int[n][m];
		int i = 0;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				for (int j = 0; j < m; j++) {
					if (j == Integer.parseInt(line)) {
						dataset[i][j] = 1;
					} else {
						dataset[i][j] = 0;
					}
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}
	/**
	 * input:path,m,n
	 * output:int[]
	 * 读取标签文件,生成一个n行矩阵
	 */
	public static int[] read_label(String path, int m, int n) {
		FileReader fr = null;
		BufferedReader br = null;
		String line = null;
		int[] dataset = new int[n];
		int i = 0;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				dataset[i] = Integer.parseInt(line);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}
	/**
	 * input:dir,train_N,test_N,n_ins,n_outs,hidden_layer_sizes
	 * 其中dir为读取文件的目录,train_N为训练集元素个数,test_N为测试集元素个数,n_ins为原始输入维度,n_outs为输出维度,hidden_layer_sizes为各个隐藏层数目
	 * 进行sdae训练及测试
	 */
	public static void sdaeTest(String dir, int train_N, int test_N, int n_ins, int n_outs, int[] hidden_layer_sizes) {
		Random rng = new Random(123);

        double pretrain_lr = 0.1;
        double corruption_level = 0.3;
        int pretraining_epochs = 1000;
        double finetune_lr = 0.1;
        int finetune_epochs = 500;
        /*
        int train_N = 6000;
        int test_N = 4000;
        int n_ins = 784;
        int n_outs = 10;
        int[] hidden_layer_sizes = {500,500,900};
        */
        int n_layers = hidden_layer_sizes.length;
        
		//String dir = "data/SA/3/";
		String train_x_path = dir + "train_x.txt";
		String train_y_path = dir + "train_y.txt";
		String test_x_path = dir + "test_x.txt";
		String test_y_path = dir + "test_y.txt";
		System.out.println("start...");
		int[][] train_x = null,test_x = null;
		int[][] train_y = null;
		int[] test_y = null;
		System.out.println("read train_x ...");
		train_x = read_x(train_x_path ,n_ins ,train_N);
		System.out.println("read train_y ...");
		train_y = read_y(train_y_path ,n_outs ,train_N);
		
		System.out.println("load SDAE ...");
		// construct SdA
        SdA sda = new SdA(train_N, n_ins, hidden_layer_sizes, n_outs, n_layers, rng);
        
        System.out.println("pretrain ...");
        // pretrain
        sda.pretrain(train_x, pretrain_lr, corruption_level, pretraining_epochs);

        System.out.println("finetune ...");
        // finetune
        sda.finetune(train_x, train_y, finetune_lr, finetune_epochs);
        
        System.out.println("read test_x ...");
        test_x = read_x(test_x_path ,n_ins ,test_N);
        System.out.println("read test_y ...");
		test_y = read_label(test_y_path ,n_outs ,test_N);
		
		double[][] test_Y = new double[test_N][n_outs];
		int count = 0,temp_l = 0;
		double temp_y = 0.0;
        // test
		System.out.println("predict ...");
        for(int i=0; i<test_N; i++) {
            sda.predict(test_x[i], test_Y[i]);
            temp_y = 0.0;
            temp_l = 0;
            for(int j=0; j<n_outs; j++) {
                System.out.print(test_Y[i][j] + " ");
                if(temp_y < test_Y[i][j]){
                	temp_y = test_Y[i][j];
                	temp_l = j;
                }
            }
            if(temp_l == test_y[i]){
            	count++;
            	System.out.println(" right ");
            }else {
            	System.out.println(" error ");
			}
            System.out.println("accuracy=" + (count * 1.0 / (i+1)) * 100 + "%");
        }
        System.out.println("accuracy="+ (count*1.0/test_N)*100 +"%");
	}
	/**
	 * input:dir,train_N,test_N,n_ins,n_outs,hidden_layer_sizes
	 * 其中dir为读取文件的目录,train_N为训练集元素个数,test_N为测试集元素个数,n_ins为原始输入维度,n_outs为输出维度,hidden_layer_sizes为各个隐藏层数目
	 * 进行ssdae训练及测试
	 */
	public static void sdaeTest2(String dir, int train_N, int test_N, int n_ins, int n_outs, int[] hidden_layer_sizes,double p,double sparsePenty) {
		Random rng = new Random(123);

        double pretrain_lr = 0.1;
        double corruption_level = 0.3;
        int pretraining_epochs = 1000;
        double finetune_lr = 0.1;
        int finetune_epochs = 500;

        /*
        int train_N = 6000;
        int test_N = 4000;
        int n_ins = 784;
        int n_outs = 10;
        int[] hidden_layer_sizes = {500,500,900};
        */
        int n_layers = hidden_layer_sizes.length;
        
		//String dir = "data/SA/3/";
		String train_x_path = dir + "train_x.txt";
		String train_y_path = dir + "train_y.txt";
		String test_x_path = dir + "test_x.txt";
		String test_y_path = dir + "test_y.txt";
		System.out.println("start...");
		int[][] train_x = null,test_x = null;
		int[][] train_y = null;
		int[] test_y = null;
		System.out.println("read train_x ...");
		train_x = read_x(train_x_path ,n_ins ,train_N);
		
		System.out.println("read train_y ...");
		train_y = read_y(train_y_path ,n_outs ,train_N);
		
		System.out.println("load SSDAE ...");
		// construct SdA
        SdA2 sda = new SdA2(train_N, n_ins, hidden_layer_sizes, n_outs, n_layers,sparsePenty, rng);
        
        System.out.println("pretrain ...");
        // pretrain
        sda.pretrain(train_x, pretrain_lr, corruption_level, pretraining_epochs,p);

        System.out.println("finetune ...");
        // finetune
        sda.finetune(train_x, train_y, finetune_lr, finetune_epochs, p);
        
        System.out.println("read test_x ...");
        test_x = read_x(test_x_path ,n_ins ,test_N);
       
        System.out.println("read test_y ...");
		test_y = read_label(test_y_path ,n_outs ,test_N);
		
		double[][] test_Y = new double[test_N][n_outs];
		int count = 0,temp_l;
		double temp_y;
		
        // test
		System.out.println("predict ...");
        for(int i=0; i<test_N; i++) {
            sda.predict(test_x[i], test_Y[i]);
            temp_l = 0;
            temp_y = 0.0;
            for(int j=0; j<n_outs; j++) {
                System.out.print(test_Y[i][j] + " ");
                if(temp_y < test_Y[i][j]){
                	temp_y = test_Y[i][j];
                	temp_l = j;
                }
            }
            if(temp_l == test_y[i]){
            	count++;
            	System.out.println(" right ");
            }else {
            	System.out.println(" error ");
			}
            System.out.println("accuracy=" + (count * 1.0 / (i+1)) * 100 + "%");
        }
        System.out.println("accuracy=" + (count*1.0/test_N)*100 +"%");
	}
	/**
	 * input:dir,train_N,test_N,n_ins,n_outs,hidden_layer_sizes
	 * 其中dir为读取文件的目录,train_N为训练集元素个数,test_N为测试集元素个数,n_ins为原始输入维度,n_outs为输出维度,hidden_layer_sizes为各个隐藏层数目
	 * 进行ssdae-wb的训练及测试
	 * 注:可以在dA3内部修改改为ssdae-w
	 */
	public static void sdaeTest3(String dir, int train_N, int test_N, int n_ins, int n_outs, int[] hidden_layer_sizes,double p,double sparsePenty) {
		Random rng = new Random(123);

        double pretrain_lr = 0.1;
        double corruption_level = 0.3;
        int pretraining_epochs = 1000;
        double finetune_lr = 0.1;
        int finetune_epochs = 500;
        
        /*
        int train_N = 6000;
        int test_N = 4000;
        int n_ins = 784;
        int n_outs = 10;
        int[] hidden_layer_sizes = {500,500,900};
        */
        int n_layers = hidden_layer_sizes.length;
        
		//String dir = "data/SA/3/";
		String train_x_path = dir + "train_x.txt";
		String train_y_path = dir + "train_y.txt";
		String test_x_path = dir + "test_x.txt";
		String test_y_path = dir + "test_y.txt";
		System.out.println("start...");
		int[][] train_x = null,test_x = null;
		int[][] train_y = null;
		int[] test_y = null;
		System.out.println("read train_x ...");
		train_x = read_x(train_x_path ,n_ins ,train_N);
		System.out.println("read train_y ...");
		train_y = read_y(train_y_path ,n_outs ,train_N);
		
		System.out.println("load SSDAE-original ...");
		// construct SdA
        SdA3 sda = new SdA3(train_N, n_ins, hidden_layer_sizes, n_outs, n_layers,sparsePenty, rng);
        
        System.out.println("pretrain ...");
        // pretrain
        sda.pretrain(train_x, pretrain_lr, corruption_level, pretraining_epochs,p);

        System.out.println("finetune ...");
        // finetune
        sda.finetune(train_x, train_y, finetune_lr, finetune_epochs, p);
        
        System.out.println("read test_x ...");
        test_x = read_x(test_x_path ,n_ins ,test_N);
        System.out.println("read test_y ...");
		test_y = read_label(test_y_path ,n_outs ,test_N);
		
		double[][] test_Y = new double[test_N][n_outs];
		int count = 0,temp_l;
		double temp_y;
		
        // test
		System.out.println("predict ...");
        for(int i=0; i<test_N; i++) {
            sda.predict(test_x[i], test_Y[i]);
            temp_l = 0;
            temp_y = 0.0;
            for(int j=0; j<n_outs; j++) {
                System.out.print(test_Y[i][j] + " ");
                if(temp_y < test_Y[i][j]){
                	temp_y = test_Y[i][j];
                	temp_l = j;
                }
            }
            if(temp_l == test_y[i]){
            	count++;
            	System.out.println(" right ");
            }else {
            	System.out.println(" error ");
			}
            System.out.println("accuracy=" + (count * 1.0 / (i+1)) * 100 + "%");
        }
        System.out.println("accuracy=" + (count*1.0/test_N)*100 +"%");
	}

	public static void main(String[] args) {
		String dir = args[0] + "/";
		int train_N,test_N,n_ins,n_outs,n_layer;
		double p,sparsePenty;
		int[] hidden_layer_sizes;
		train_N = Integer.parseInt(args[1]); //训练集元素个数
		test_N = Integer.parseInt(args[2]);  //测试集元素个数
		n_ins = Integer.parseInt(args[3]);   //原始输入维度
		n_outs = Integer.parseInt(args[4]);  //模型输出维度，即分类个数
		n_layer = Integer.parseInt(args[5]); //多少个隐藏层
		hidden_layer_sizes = new int[n_layer];
		int i;
		for(i = 0;i < n_layer;i++){
			hidden_layer_sizes[i] = Integer.parseInt(args[6+i]);  //第i个隐藏层层数
		}
		p = Double.parseDouble(args[i]);     //稀疏性参数
		sparsePenty = Double.parseDouble(args[i+1]);         //稀疏惩罚
		//sdaeTest(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes); 
		//sdaeTest2(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes,p,sparsePenty);
		sdaeTest3(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes,p,sparsePenty);
	}

}
