package org.groupTw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JPanel {
    ArrayList<JButton> buttons;
    JPanel panel;
    final static int X_BUTTON_MENU = AppFrame.XSPAN/3;
    final static int Y_BUTTON_MENU = AppFrame.YSPAN/10;

    public Menu(){
        createButtons();
        this.setLayout(new FlowLayout(FlowLayout.CENTER,(AppFrame.XSPAN - X_BUTTON_MENU)/2,(AppFrame.YSPAN - (this.buttons.size() * Y_BUTTON_MENU))/2));
        this.setPreferredSize(new Dimension(AppFrame.XSPAN,AppFrame.YSPAN));
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        this.add(panel);
        for( JButton btn : buttons ){
            panel.add(btn);
        }
    }

    private void createButtons(){
        this.buttons = new ArrayList<>();
        for( String label : new String[]{"New Game", "Settings","Create Your Map"} ) {
            JButton btn = new JButton(label);
            btn.setActionCommand(label);
            btn.addActionListener(new ButtonAction());
            btn.setPreferredSize( new Dimension(X_BUTTON_MENU,Y_BUTTON_MENU));
            btn.setMaximumSize( new Dimension(X_BUTTON_MENU,Y_BUTTON_MENU));
            buttons.add(btn);
        }

        JButton quit = new JButton("Quit");
        quit.addActionListener(new ButtonAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        quit.setPreferredSize( new Dimension(X_BUTTON_MENU,Y_BUTTON_MENU));
        quit.setMaximumSize( new Dimension(X_BUTTON_MENU,Y_BUTTON_MENU));
        buttons.add(quit);

    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            sentToFrame(e.getActionCommand());
        }
    }

    private void sentToFrame(String action_){
        AppFrame ancestorFrame = (AppFrame)SwingUtilities.getWindowAncestor(this);
        ancestorFrame.updateFrame(action_);

    }
}