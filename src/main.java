import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        //System.out.println("hello");
        File logFolder = new File("./inp");
        File[] dirs = logFolder.listFiles();
        List<File> inputFiles = new ArrayList<>();
        if (dirs == null) {
            return;
        }
        for (File dir : dirs) {
            if (dir.isDirectory()){
                System.out.println("Processing folder: " + dir.getName());
                File[] files = dir.listFiles();
                for (File f : files) {
                    System.out.println("Processing file: " + f.getName());
                    if(f.getName().endsWith(".inp")){
                        inputFiles.add(f);
                        System.out.println("Added file: " + f.getName());
                    }
                }
            }
            System.out.println("No. of file .inp: " + inputFiles.size());
        }

        for (File input: inputFiles) {
            try {
                FileReader fileReader = new FileReader(input);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}