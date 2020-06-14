package org.groupTw;

import org.groupTw.MapEnitites.Entity;
import org.groupTw.MapEnitites.EntityFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CreatorMap extends JPanel {
    private GameLayout mapLayout;
    private JPanel chooseMenuPlayer1;
    private JPanel chooseMenuPlayer2;
    static private ArrayList<Entity> prototypes = new ArrayList<>();
    static private int entityToPlace = -1;
    private ButtonGroup btnGroup;
    private JPanel buttonPanel;

    public CreatorMap(GameLayout mapLayout) {
        this.mapLayout = mapLayout;
        this.chooseMenuPlayer1 = new JPanel(new GridLayout());
        this.chooseMenuPlayer2 = new JPanel(new GridLayout());
        this.buttonPanel = new JPanel();
        this.btnGroup = new ButtonGroup();
        chooseMenuPlayer1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        chooseMenuPlayer2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        initView();
    }

    private void initView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        buttonPanel.setLayout( new BoxLayout(buttonPanel,BoxLayout.Y_AXIS) );
        JPanel secondaryLayout = new JPanel();
        secondaryLayout.setLayout(new BoxLayout(secondaryLayout,BoxLayout.X_AXIS));
        this.add(secondaryLayout);
        addPlayerChooseButtons();
        secondaryLayout.add(buttonPanel);

        secondaryLayout.add(mapLayout);

        //creating prototypes
        createPrototypes();

        //place prototypes within chooseMenu
        createChooseMenu();

        this.add(chooseMenuPlayer1);
        repaint();


    }

    private void createChooseMenu()
    {
        AtomicInteger i = new AtomicInteger();
        CreatorMap.prototypes.forEach(unit -> {
            MapPanel unitPanel = new MapPanel();
            unitPanel.setEntity_on_tile(unit);
            unitPanel.add(new JLabel(unit.getPicLabel()));
            unitPanel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e) {
                    MapPanel source = (MapPanel) e.getSource();
                    Entity entityClicked = source.getEntity_on_tile();
                    for (int i = 0; i < CreatorMap.prototypes.size() - 1; i++)//search grid to get prototype index
                    {
                        if (CreatorMap.prototypes.get(i).equals(entityClicked)) {
                            CreatorMap.entityToPlace = i;
                            break;
                        }
                    }
                    System.out.println("Entity to place:" + CreatorMap.prototypes.get(entityToPlace).toString());
                }

            });
            if(i.get() < CreatorMap.prototypes.size()/2)
                chooseMenuPlayer1.add(unitPanel);
            else
                chooseMenuPlayer2.add(unitPanel);
            i.getAndIncrement();
        });
    }

    private void createPrototypes()
    {
        EntityFactory factory = new EntityFactory();
        CreatorMap.prototypes.add(factory.addEntity("archer", "blue"));
        CreatorMap.prototypes.add(factory.addEntity("warrior", "blue"));
        CreatorMap.prototypes.add(factory.addEntity("archer tower", "blue"));
        CreatorMap.prototypes.add(factory.addEntity("archer", "red"));
        CreatorMap.prototypes.add(factory.addEntity("warrior", "red"));
        CreatorMap.prototypes.add(factory.addEntity("archer tower", "red"));
    }

    private void addPlayerChooseButtons( ){
        ActionListener listener = event -> {
            JRadioButton btn = (JRadioButton)event.getSource();
            switch(btn.getName()){
                case "Player 1":
                    this.remove(chooseMenuPlayer2);
                    this.add(chooseMenuPlayer1);
                    this.repaint();
                    System.out.println("Player1");
                    break;
                case "Player 2":
                    this.remove(chooseMenuPlayer1);
                    this.add(chooseMenuPlayer2);
                    revalidate();
                    repaint();
                    chooseMenuPlayer2.repaint();
                    System.out.println("Player2");
                    break;
            }
        };

        for(String str : new String[]{"Player 1", "Player 2"}){
            JRadioButton btn = new JRadioButton(str, str.equals("Player 1"));
            btn.setName(str);
            btn.addActionListener(listener);
            btnGroup.add(btn);
            buttonPanel.add(btn);
        }

    }
    public static int getEntityToPlace() {
        return entityToPlace;
    }


}