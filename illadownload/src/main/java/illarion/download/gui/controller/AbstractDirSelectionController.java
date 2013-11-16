package illarion.download.gui.controller;

import illarion.common.util.DirectoryManager;
import illarion.download.gui.model.GuiModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public abstract class AbstractDirSelectionController implements Controller {

    @FXML
    public TextField selectedDirectory;

    @FXML
    public RadioButton optionAbsolute;

    @FXML
    public RadioButton optionRelative;

    private GuiModel model;

    @Nonnull
    private final DirectoryManager.Directory dir;

    protected AbstractDirSelectionController(@Nonnull DirectoryManager.Directory directory) {
        dir = directory;
    }

    public void initialize() {
        final DirectoryManager dm = DirectoryManager.getInstance();
        if (dm.isDirectorySet(dir)) {
            //noinspection ConstantConditions
            selectedDirectory.setText(dm.getDirectory(dir).getAbsolutePath());
        }

        if (dm.isRelativeDirectoryPossible()) {
            optionAbsolute.setSelected(!dm.isDirectoryRelative(dir));
            optionRelative.setSelected(dm.isDirectoryRelative(dir));
        } else {
            optionAbsolute.setSelected(true);
            optionRelative.setDisable(true);
        }
    }

    @FXML
    public void browse(@Nonnull final ActionEvent actionEvent) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        final DirectoryManager dm = DirectoryManager.getInstance();
        if (dm.isDirectorySet(dir)) {
            directoryChooser.setInitialDirectory(dm.getDirectory(dir));
        } else {
            final File subDir = new File(System.getProperty("user.home"), "Illarion");
            directoryChooser.setInitialDirectory(new File(subDir, dir.getDefaultDir()));
        }
        while (!directoryChooser.getInitialDirectory().exists()) {
            directoryChooser.setInitialDirectory(directoryChooser.getInitialDirectory().getParentFile());
        }

        final File selectedDirectory = directoryChooser.showDialog(model.getStage());

        this.selectedDirectory.setText(selectedDirectory.getAbsolutePath());
        optionAbsolute.setSelected(true);
    }

    @Override
    public void setModel(@Nonnull GuiModel model) {
        this.model = model;
    }

    public void nextStep(@Nonnull final ActionEvent actionEvent) {
        final DirectoryManager dm = DirectoryManager.getInstance();
        if (optionRelative.isSelected() && dm.isRelativeDirectoryPossible()) {
            dm.setDirectoryRelative(dir);
        } else {
            final File targetDir = new File(selectedDirectory.getText());
            dm.setDirectory(dir, targetDir);
        }
        try {
            model.getStoryboard().nextScene();
        } catch (@Nonnull final IOException e) {
            // nothing
        }
    }
}
