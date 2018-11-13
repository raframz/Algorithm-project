package manager;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Manager {

    private final File commander;
    private final Vector keyCommand;
    private final Vector variable;
    private final Vector filename;
    private RandomAccessFile tool;
    private final Hashtable<String, FileEspecial> files;
    private static int LINE = 0;

    public Manager() {
        variable = new Vector();
        keyCommand = new Vector();
        keyCommand.add("assign");
        keyCommand.add("create");
        keyCommand.add("sort");
        keyCommand.add("rem_doubles");
        commander = new File("C:\\Users\\raframz\\Desktop\\Manager Files\\command.txt");
        files = new Hashtable<>();
        filename = new Vector();
    }

    //COMPILAR
    public void compiler(String line) throws FileNotFoundException {
        try {
            String[] syntax = line.split(" ");
            LINE++;
            System.out.println("Evaluating: " + Arrays.toString(syntax));
            if (syntax[0] != null) {

                if (syntax[0].equals("create") && syntax[1] != null) { // Si vamos a crear
                    String name = (syntax[1].substring(1, syntax[1].length() - 1));
                    create(syntax, name);
                    variable.add(syntax[3]);
                    filename.add(name);

                } else if (syntax[0].equals("assign")) { // Si vamos a assignar
                    String name = (syntax[1].substring(1, syntax[1].length() - 1));
                    assign(syntax, name);
                    variable.add(syntax[3]);

                } else if (variable.contains(syntax[0]) && syntax[1].equals("=")) {
                    if (syntax.length == 5) {
                        if (syntax[2].equals("sort") && syntax[4].equals("asc")) {
                            sort(syntax);
                        } else if (variable.contains(syntax[2]) && variable.contains(syntax[4])) {
                            joinfiles(syntax);
                        }
                    } else if (syntax.length == 4 && syntax[2].equals("rem_doubles") && variable.contains(syntax[3])) {
                        rem_doubles(syntax);

                    } else if (syntax.length == 3 && variable.contains(syntax[2])) {
                        equalize(syntax);

                    } else if (!keyCommand.contains(syntax[0]) || keyCommand.contains(syntax[2])) {
                        System.out.println("Syntax Error At Line " + LINE + " please check your commands");
                        System.exit(0);
                    } else {
                        System.out.println("Syntax Error At Line " + LINE + " please check your commands");
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Please check your entry");
            System.exit(0);

        }

    }

    //CREAR
    public void create(String[] line, String name) throws FileNotFoundException {
            if (line.length < 4 || line.length > 4) {
                System.out.println("Syntax Error");
            } else if (!line[2].equals("as") || line[3].isEmpty()) {
                System.out.println("Syntax Error");
            }
            FileEspecial f = new FileEspecial(name, line[3]);
            files.put(line[3], f);
            System.out.println("A NEW FILE HAS BEEN CREATED:\n" + f.toString() + "\n\n");

    }

    //ASIGNAR
    public void assign(String[] line, String name) throws FileNotFoundException {
            if (line.length < 4 || line.length > 4) {
                System.out.println("Syntax Error");
            }
            if (!line[2].equals("to") || line[3].isEmpty()) {
                System.out.println("Syntax Error");
            }
            if (filename.contains(name)) {
                Enumeration<String> keys = files.keys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    //    si el nombre de un archivo          
                    if (files.get(key).getName().equals(name)) {
                        System.out.println("THE FILE ID HAS BEEN MODIFIED: \n"
                                + files.get(key).toString());
                        files.get(key).setId(line[3]);
                        System.out.println("NOW WITH THE NEW ID: \n" + files.get(key).toString() + "\n\n");
                    }
                }
            } else {
                System.out.println("The File " + name + " is not in the directory");
            }
    }

    //SORT
    public void sort(String[] line) {
        Enumeration<String> keys = files.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (files.get(key).getId().equals(line[3])) {
                files.get(line[0]).fillFile(files.get(key).sortasc());
                System.out.println("THE CONTENT HAS BEEN REASSIGNED \n" + files.get(line[0]).toString() + "\n\n");
            }
        }

    }

    //REM_DOUBLES
    public void rem_doubles(String[] line) {
        String key = "";
        int[] aux = new int[FileEspecial.SIZE];
        Enumeration<String> keys = files.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if (files.get(key).getId().equals(line[3])) {
                aux = files.get(key).rem_doublesArr();
            }
        }
        key = "";
        Enumeration<String> keyss = files.keys();
        while (keyss.hasMoreElements()) {
            key = keyss.nextElement();
            if (files.get(key).getId().equals(line[0])) {
                files.get(key).fillFile(aux);
                System.out.println("THE CONTENT HAS BEEN CHANGED: \n" + files.get(key).toString());
            }
        }

        System.out.println("THIS TIME WITHOUT REPEATED ELEMENTS\n\n");
    }

    //SET EQUALS
    public void equalize(String[] line) {
        String key = "";
        int[] aux = new int[FileEspecial.SIZE];
        Enumeration<String> keys = files.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if (files.get(key).getId().equals(line[2])) {
                aux = files.get(key).getContent();
            }
        }
        key = "";
        Enumeration<String> keyss = files.keys();
        while (keyss.hasMoreElements()) {
            key = keyss.nextElement();
            if (files.get(key).getId().equals(line[0])) {
                files.get(key).fillFile(aux);
            }
        }

        System.out.println("THE CONTENT HAS BEEN ASSIGNED "
                + line[2] + " INTO THE FILE " + line[0] + "\n\n");
    }

//SUMAR VARIABLES
    public void joinfiles(String[] line) {
        String key = "";
        int[] contentA = new int[FileEspecial.SIZE];
        int[] contentB = new int[FileEspecial.SIZE];
        int[] contentFinal;
        Enumeration<String> keys = files.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if (files.get(key).getId().equals(line[2])) {
                contentA = files.get(key).getContent();
            }
            if (files.get(key).getId().equals(line[4])) {
                contentB = files.get(key).getContent();
            }
        }
        key = "";
        contentFinal = new int[contentA.length + contentB.length];
        int b = contentB.length;
        for (int i = 0; i < contentA.length; i++) {
            contentFinal[i] = contentA[i];
        }
        for (int i = 0; i < contentB.length; i++) {
            contentFinal[b] = contentB[i];
            b++;
        }
        Enumeration<String> keyss = files.keys();
        while (keyss.hasMoreElements()) {
            key = keyss.nextElement();
            if (files.get(key).getId().equals(line[0])) {
                files.get(key).fillFile(contentFinal);
                System.out.println("THE CONTENT HAS BEEN UNITED "
                        + line[2] + " & " + line[4] + "\nEN THE FILE: " + files.get(key).toString());
            }
        }

    }

    // LEER
    public void reader() throws FileNotFoundException, IOException {
        String x = "";
        int line = 0;
        tool = new RandomAccessFile(commander, "rw");
        while ((x = tool.readLine()) != null) {
            compiler(x);
        }
    }

    public static void main(String[] args) throws IOException {
        Manager m = new Manager();
        m.reader();

    }

}

/*
create "ans.txt" as file_out
create "tmp.txt" as tmp
assing "test.txt" to file_in
tmp = sort file_in asc
tmp = rem_doubles tmp
file_out = tmp

 */
