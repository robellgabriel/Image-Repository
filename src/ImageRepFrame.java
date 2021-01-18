import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageRepFrame extends JFrame implements ImageRepView {

    private final JTextField status;
    private final JList<String> imageJList;
    private final DefaultListModel<String> imageList;


    public ImageRepFrame(){
        super("Image Repository");
        ImageRepModel dlm = new ImageRepModel();
        dlm.addImageRepView(this);

        JButton add = new JButton("add");
        JButton remove = new JButton("remove");
        status = new JTextField();
        status.setEditable(false);
        imageList = new DefaultListModel<>();
        imageJList = new JList<>(imageList);
        JScrollPane sp = new JScrollPane(imageJList);

        add.addActionListener(e->{
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            JFileChooser file = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Image (*.jpg,*.jpeg,*.png,*.gif,*.bmp,*.wbmp)", "jpg", "jpeg", "png", "gif", "bmp", "wbmp");
            file.setFileFilter(filter);
            file.setMultiSelectionEnabled(true);
            file.setAcceptAllFileFilterUsed(false);
            int returnVal = file.showSaveDialog(this);
            if (file.getSelectedFile() == null || returnVal == JFileChooser.CANCEL_OPTION){
                return;
            }
            if (dlm.addImage(Arrays.asList(file.getSelectedFiles()))) {
                for (File f1 : file.getSelectedFiles()) {
                    imageList.addElement(f1.getName());
                }
            }
            dlm.update();

        });
        remove.addActionListener(e->{
            if (!imageJList.isSelectionEmpty()){
                dlm.removeImage(imageJList.getSelectedValuesList());

                for (String name : imageJList.getSelectedValuesList()){
                    imageList.removeElement(name);
                }
            }
            dlm.update();
        });
        imageJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount()==2){
                    JFrame f = new JFrame(imageJList.getSelectedValue());
                    JLabel label = new JLabel();
                    label.setSize(900,900);
                    BufferedImage img = null;
                    try {
                        img = dlm.openImage(imageJList.getSelectedValue());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    assert img != null;
                    Image dimg = img.getScaledInstance(label.getWidth(), label.getHeight(),
                            Image.SCALE_SMOOTH);
                    ImageIcon ii = new ImageIcon(dimg);
                    label.setIcon(ii);
                    f.add(label, BorderLayout.CENTER);
                    f.setSize(label.getSize());
                    f.setVisible(true);
                }
            }
        });

        this.setLayout(new GridLayout(2,2));
        this.add(add);
        this.add(remove);
        this.add(status);
        this.add(sp);
        this.setSize(600,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void handleImageRepUpdate(ImageRepEvent e) {
        if (e.getStatus() == ImageRepModel.Status.ADD) {
            status.setText("Image(s) successfully added to repository!");
        }else if (e.getStatus() == ImageRepModel.Status.REMOVE){
            status.setText("Image(s) successfully removed from repository!");
        }else if (e.getStatus() == ImageRepModel.Status.ERROR){
            status.setText("Duplicate image names are not allowed");
        }
    }

    public static void main(String[] args) {
        new ImageRepFrame();
    }
}
