package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileEspecial {
    final static int SIZE = 8; 
    private final RandomAccessFile tool;
    private File xFile;
    private String name;
    private String id;
    private final int[] content;
    FileEspecial f;

    public FileEspecial(String name, String id) throws FileNotFoundException {
        this.name = name;
        content = new int[SIZE];
        this.xFile = new File("C:\\Users\\raframz\\Desktop\\Manager Files\\" + name);
        tool = new RandomAccessFile(this.xFile, "rw");
        this.id = id;
        fillFile();
        f = this;
    }

    
    public int[] rem_doublesArr() {
        int [] aux =  new int[content.length];
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < content.length; j++) {
                if(!isIn(content[j], aux)){
                    aux[i] = content[j];                    
                }
            }
        }
        return aux;        
    }
    
    public boolean isIn(int key, int [] arr){
        boolean x = false;
        for (int i = 0; i < arr.length; i++) {
            if (key == arr[i]) {
                x = true;
            } 
        }
        return x;
    }

    public int[] sortasc() {
        int aux = 0;
        for (int i = 0; i < content.length; i++) {
            for (int j = i + 1; j < content.length; j++) {
                if (content[i] < content[j]) {
                    aux = content[i];
                    content[i] = content[j];
                    content[j] = aux;
                }
            }
        }
        return content;
    }

    public FileEspecial getFileEspecial() {
        return this;
    }

    private int[] fillArr() {
        for (int i = 0; i < content.length; i++) {
            int number = (int) (Math.random() * 1000) + 1;
            content[i] = number;
        }
        return content;
    }

    private void fillFile() {
        fillArr();
        try (RandomAccessFile r = new RandomAccessFile(xFile, "rw")) {
            for (int i = 0; i < content.length; i++) {
                r.write(content[i]);
            }
        } catch (Exception e) {
        }
    }

    public void fillFile(int[] content) {
        try (RandomAccessFile r = new RandomAccessFile(xFile, "rw")) {
            for (int i = 0; i < content.length; i++) {
                if(content[i]!=0){
                r.write(content[i]);
                }
            }
        } catch (Exception e) {
        }
    }

    public File getX() {
        return xFile;
    }

    public void setX(File xFile) {
        this.xFile = xFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getContent() {
        return content;
    }   

    public String toString() {
        return  "File Name: " + getName()+" - - File ID: " + getId();
    }

}
