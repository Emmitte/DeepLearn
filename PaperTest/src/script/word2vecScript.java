package script;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import word2vec.main.java.com.ansj.vec.Word2VEC;
import word2vec.main.java.com.ansj.vec.domain.WordEntry;

public class word2vecScript {
	
	public static byte[] bt;
	public static InputStream is;
	public static Reader read;
	public static Lexeme t;
	public static IKSegmenter iks;
	
	public static List<String> readDic(String path) {
		List<String> list = new ArrayList<String>();
		FileReader fr=null;
		BufferedReader br=null;
		String line=null;
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
	
	public static void string2vec(Word2VEC w, String readPath, String dicPath, String writePath) {
		FileReader fr=null;
		BufferedReader br=null;
		String line=null;
		FileOutputStream fos=null;
		PrintStream ps=null;
		List<String> dimList = readDic(dicPath);
		int n = dimList.size();
		Set<WordEntry> temp;
		Iterator iter;
		WordEntry entry;
		try {
			fr = new FileReader(readPath);
			br = new BufferedReader(fr);
			fos = new FileOutputStream(writePath);
			ps = new PrintStream(fos);
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
					//word2vec¿©≥‰¥ “Â
					temp = w.distance(t.getLexemeText());
					iter = temp.iterator();
					while(iter.hasNext()){
						entry = (WordEntry) iter.next();
						if(dimList.contains(entry.name)){
							dimArray[dimList.indexOf(entry.name)]++;
						}
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
	
	public static void enlargeDic(Word2VEC w, String dicPath, String writePath) {
		FileOutputStream fos=null;
		PrintStream ps=null;
		List<String> dimList = readDic(dicPath);
		List<String> list = new ArrayList<String>();
		Set<WordEntry> temp;
		Iterator iter;
		WordEntry entry;
		int i,n;
		n = dimList.size();
		try {
			fos = new FileOutputStream(writePath);
			ps = new PrintStream(fos);
			for(i = 0;i < n;i++){
				if(!list.contains(dimList.get(i))){
					list.add(dimList.get(i));
				}
				temp = w.distance(dimList.get(i));
				iter = temp.iterator();
				while(iter.hasNext()){
					entry = (WordEntry) iter.next();
					if(!list.contains(entry.name)){
						list.add(entry.name);
					}
				}
			}
			n = list.size();
			for(i = 0;i < n;i++){
				ps.print(list.get(i)+" ");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
    public static void main(String[] args) throws IOException {
        Word2VEC w = new Word2VEC() ;
        w.loadGoogleModel("model/vectors-sougou.bin") ;
        
        String dir = "data/word2vec/text/";
        String dicPath = dir + "dic.txt";
        String readPath = dir + "train_x_new.txt";
        String writePath = dir + "vector.txt";
        //String writePath = dir + "newdic.txt";
        string2vec(w, readPath, dicPath, writePath);
        //enlargeDic(w1, dicPath, writePath);
        
    }
}
