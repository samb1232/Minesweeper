import javax.swing.*;
import java.awt.*;

public enum Status {
    WIN(new ImageIcon("resources/smile_win.png").getImage()),
    IN_GAME(new ImageIcon("resources/smile_in_game.png").getImage()),
    LOSE(new ImageIcon("resources/smile_lose.png").getImage()),
    SOLVING(new ImageIcon("resources/smile_think.png").getImage());

    Image image;
    Status(Image image) {
        this.image = image;
    }
}
