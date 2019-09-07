package utility;
import java.io.*;
import java.util.ArrayList;

public class SourceReader {
   private static SourceReader instance=null;
    private SourceReader(){}
    public static SourceReader getInstance(){
        if(instance==null)instance=new SourceReader();
        return instance;
    }
    public ArrayList<String> readFile(String filePath){
        File file = new File(filePath);

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<String> lines=new ArrayList<>();
            String currentLine;
            while ((currentLine = br.readLine()) != null){lines.add(currentLine);}
            br.close();
            return lines;
        }catch(Exception e){
            System.err.println(e);
        }
        return null;
    }
    public void writeFile(ArrayList<String> fileLines,String filePath){
        File file = new File(filePath);

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for(String s:fileLines){
                if(s.equals(""))continue;
                bw.write(s);
                bw.newLine();

            }
            bw.close();
        }catch(Exception e){
            System.err.println(e);
        }
    }

}
