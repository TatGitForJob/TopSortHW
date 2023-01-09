import java.io.File;
import java.util.ArrayList;

/*
This class contains text file and the files it depends on
 */
class Vertex{
public Vertex(File file){
     file_=file;
    parents=new ArrayList<>();
}
    private final File file_;

    public ArrayList<Integer> getParents() {
        return parents;
    }

    public File getFile() {
        return file_;
    }

    public void setParents(ArrayList<Integer> parents) {
        this.parents = parents;
    }

    private ArrayList<Integer> parents;

    public void appendParent(int index){
        parents.add(index);
    }
    /*
    Find index of input file in input Vertex list
     */
    public static int containVertex(ArrayList<Vertex> array,File file){
        for (int i=0;i<array.size();i++) {
            if (array.get(i).getFile().toString().equals(file.toString())) {
                return i;
            }
        }
        return -1;
    }
}
