package app.gui;

import app.dto.azure.recive.group.GetPersonDto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;

public class ContactTreeCellFactory extends TreeCell<GetPersonDto> {

    private ContextMenu nodeMenu;

    public ContactTreeCellFactory(){
        nodeMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add face");
        nodeMenu.getItems().add(addMenuItem);
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Faca");
            }
        });
    }

    public ContactTreeCellFactory(ContextMenu nodeMenu) {
        this.nodeMenu = nodeMenu;
    }


    @Override
    protected void updateItem(GetPersonDto item, boolean empty) {
        super.updateItem(item,empty);
        if (!empty){
            setText(item.toString());

            setContextMenu(nodeMenu);
        }
    }



}
