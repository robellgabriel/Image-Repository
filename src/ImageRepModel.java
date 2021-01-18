import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageRepModel {

    private final List<File> imgCollection;
    private final List<ImageRepView> imageRepViews;
    public enum Status {ADD,REMOVE,NONE,ERROR}
    private Status status;


    public ImageRepModel() {
        imgCollection = new ArrayList<>();
        imageRepViews = new ArrayList<>();
        status = Status.NONE;

    }

    public boolean addImage(List<File> files){
        for (File newFile : files){
            for (File savedFile : imgCollection) {
                if (newFile.getName().equals(savedFile.getName())) {
                    status = Status.ERROR;
                    return false;
                }
            }
        }
        imgCollection.addAll(files);
        status = Status.ADD;
        return true;
    }

    public void removeImage(List<String> files){
        for (String name : files){
            imgCollection.removeIf(f1 -> name.equals(f1.getName()));
        }
        status = Status.REMOVE;
    }

    public BufferedImage openImage(String fileName) throws IOException {
        for (File file : imgCollection){
            if (fileName.equals(file.getName())){
                return ImageIO.read(file);
            }
        }
        return null;
    }

    public void addImageRepView(ImageRepView irv){ imageRepViews.add(irv);}

    public void update(){
        for (ImageRepView irv: imageRepViews){
            irv.handleImageRepUpdate(new ImageRepEvent(this, status));
        }
    }

}

