import java.util.EventObject;

public class ImageRepEvent extends EventObject {

    private ImageRepModel.Status status;

    public ImageRepEvent(ImageRepModel imageRepModel, ImageRepModel.Status status){
        super(imageRepModel);
        this.status = status;
    }

    public ImageRepModel.Status getStatus(){return status;}

}


