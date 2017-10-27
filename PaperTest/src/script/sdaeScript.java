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
	 * ��ȡ����(x)�ļ�,����һ��n��m�еľ���
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
	 * ��ȡ��ǩ(y)�ļ�,����һ��n��m�еľ���,����:��ǩΪ0->100,1->010,2->001
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
	 * ��ȡ��ǩ�ļ�,����һ��n�о���
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
	 * ����dirΪ��ȡ�ļ���Ŀ¼,train_NΪѵ����Ԫ�ظ���,test_NΪ���Լ�Ԫ�ظ���,n_insΪԭʼ����ά��,n_outsΪ���ά��,hidden_layer_sizesΪ�������ز���Ŀ
	 * ����sdaeѵ��������
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
	 * ����dirΪ��ȡ�ļ���Ŀ¼,train_NΪѵ����Ԫ�ظ���,test_NΪ���Լ�Ԫ�ظ���,n_insΪԭʼ����ά��,n_outsΪ���ά��,hidden_layer_sizesΪ�������ز���Ŀ
	 * ����ssdaeѵ��������
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
	 * ����dirΪ��ȡ�ļ���Ŀ¼,train_NΪѵ����Ԫ�ظ���,test_NΪ���Լ�Ԫ�ظ���,n_insΪԭʼ����ά��,n_outsΪ���ά��,hidden_layer_sizesΪ�������ز���Ŀ
	 * ����ssdae-wb��ѵ��������
	 * ע:������dA3�ڲ��޸ĸ�Ϊssdae-w
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
		train_N = Integer.parseInt(args[1]); //ѵ����Ԫ�ظ���
		test_N = Integer.parseInt(args[2]);  //���Լ�Ԫ�ظ���
		n_ins = Integer.parseInt(args[3]);   //ԭʼ����ά��
		n_outs = Integer.parseInt(args[4]);  //ģ�����ά�ȣ����������
		n_layer = Integer.parseInt(args[5]); //���ٸ����ز�
		hidden_layer_sizes = new int[n_layer];
		int i;
		for(i = 0;i < n_layer;i++){
			hidden_layer_sizes[i] = Integer.parseInt(args[6+i]);  //��i�����ز����
		}
		p = Double.parseDouble(args[i]);     //ϡ���Բ���
		sparsePenty = Double.parseDouble(args[i+1]);         //ϡ��ͷ�
		//sdaeTest(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes); 
		//sdaeTest2(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes,p,sparsePenty);
		sdaeTest3(dir, train_N, test_N, n_ins, n_outs, hidden_layer_sizes,p,sparsePenty);
	}

}
