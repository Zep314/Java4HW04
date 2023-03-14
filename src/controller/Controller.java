package controller;

//import model.DoublyList;
import view.View;
import model.RBTree;
import model.Item;

public class Controller {  // Контроллер
    private View view;  // Вьювер
    private RBTree rbt;

    public Controller() {
        this.view = new View();
        this.rbt = new RBTree();
    }
    public void run(){
        view.print("============= Начало работы =============");
        this.rbt.insertItem(17);
        this.rbt.insertItem(9);
        this.rbt.insertItem(81);
        this.rbt.insertItem(3);
        this.rbt.insertItem(18);
        this.rbt.insertItem(12);
        this.rbt.insertItem(19);
        this.rbt.insertItem(24);
        this.rbt.insertItem(75);

        this.printTree(0, rbt.getRoot());

        this.rbt.deleteItem(17);
        view.print("============= Удаление корня =============");
        this.printTree(0, rbt.getRoot());
        view.print("============= Конец работы =============");
    }

    public void printTree(int level,Item item) {
        view.print(String.format("Level: %d, %s", level, item.toString()));
        if (item.getLeft() != null) {
            this.printTree(level+1, item.getLeft());
        }
        if (item.getRight() != null) {
            this.printTree(level +1,item.getRight());
        }
    }
}
