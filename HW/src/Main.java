import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    //list of find text files.
    static ArrayList<Vertex> vertexArray = new ArrayList<>();

    //list of final text files in the correct sequence
    static ArrayList<String> finalArray = new ArrayList<>();

    // flag to check cycles
    static int iterationsCounter = 0;

    // path to input directory
    static String path;

    /*
     * method of processing a vertex and its parents recursively and adding it to the search list
     * in:
     * vertex: text file as object class Vertex
     */
    public static void appendFile(Vertex vertex) {
        iterationsCounter++;
        if (iterationsCounter >= vertexArray.size() * vertexArray.size()) {
            return;
        }
        if (vertex.getParents().isEmpty()) {
            if (!finalArray.contains(vertex.getFile().toString())) {
                finalArray.add(vertex.getFile().toString());
            }
            return;
        }
        for (int parent : vertex.getParents()) {
            appendFile(vertexArray.get(parent));
        }
        if (!finalArray.contains(vertex.getFile().toString())) {
            finalArray.add(vertex.getFile().toString());
        }
    }

    /*
     *sort all found files to a new list
     */
    public static void makeNiceArray() {
        for (Vertex vertex : vertexArray) {
            appendFile(vertex);
        }
    }

    /*
    Read input file and find sequences in it
     */
    public static void readFile(File file) {
        try {
            Vertex vertex = new Vertex(file);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String content = reader.readLine();

                if (content.startsWith("require")) {
                    String substring = File.separator + content.substring(9, content.length() - 1);
                    File parent = new File(path + substring);
                    if (Vertex.containVertex(vertexArray, parent) == -1) {
                        vertexArray.add(new Vertex(parent));
                        vertex.appendParent(vertexArray.size() - 1);
                    } else {
                        vertex.getParents().add(Vertex.containVertex(vertexArray, parent));
                    }
                }

            }

            if (Vertex.containVertex(vertexArray, vertex.getFile()) == -1) {
                vertexArray.add(vertex);
            } else {
                for (Vertex tmp : vertexArray) {
                    if (tmp.getFile().toString().equals(vertex.getFile().toString()))
                        tmp.setParents(vertex.getParents());
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Directory validation and reading all files in it
     */
    public static void checkDirectory(File directory) {
        File[] files = directory.listFiles();
        assert files != null;
        if (files.length == 0) {
            System.out.println("Пустая директория:" + directory);
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                readFile(file);
                continue;
            }

            if (file.isDirectory()) {
                checkDirectory(file);
            }
        }
    }

    /*
    Reading path to input directory, checking it to correct and to list files and their contents out to the console
     */
    public static void main(String[] args) {
        System.out.println("Введите абсолютный путь до необходимой директории");
        Scanner keyboard = new Scanner(System.in);
        path = keyboard.nextLine();
        if (!new File(path).exists()) {
            System.out.println("Введенная директория не найдена");
            return;
        }
        if (!new File(path).isDirectory()) {
            System.out.println("Введенный путь не ведет к директории");
            return;
        }
        checkDirectory(new File(path));
        makeNiceArray();
        if (iterationsCounter >= vertexArray.size() * vertexArray.size()) {
            System.out.println("Обнаружен цикл в зависимостях");
            return;
        }
        for (String file : finalArray)
            System.out.println(file);
        System.out.println("Теперь содержимое:");
        for (String file : finalArray) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    System.out.println(reader.readLine());
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}