package map;

import javax.swing.*;

public class MyJTextPanel extends JTextPane {
    public MyJTextPanel(){
        super();
    }

    @Override
    public boolean getScrollableTracksViewportWidth(){
        return false;
    }
}
