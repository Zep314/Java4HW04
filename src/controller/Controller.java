package controller;

import view.View;
import model.RBTree;
import model.Item;

public class Controller {  // Контроллер
    private final View view;  // Вьювер
    private final RBTree rbt;  // Структура красно-черного дерева

    public Controller() {
        this.view = new View();
        this.rbt = new RBTree();
    }
    public void run(){
        view.print("============= Начало работы =============");
        this.rbt.insertItem(17);  // Заполнение дерева данными
        this.rbt.insertItem(9);
        this.rbt.insertItem(81);
        this.rbt.insertItem(3);
        this.rbt.insertItem(18);
        this.rbt.insertItem(12);
        this.rbt.insertItem(19);
        this.rbt.insertItem(24);
        this.rbt.insertItem(75);

        this.printTree(0, rbt.getRoot());  // Визуализация дерева

        view.print("============= Удаление корня =============");
        this.rbt.deleteItem(17);   // Удаляем элемент (корень - так интереснее)
        this.printTree(0, rbt.getRoot());  // Визуализация дерева

        view.print("============= Конец работы =============");
    }

    public void printTree(int level,Item item) {  // Рекурсивно выводим элементы дерева
        view.print(String.format("Level: %d, %s", level, item.toString()));
        if (item.getLeft() != null) {
            this.printTree(level+1, item.getLeft());  // Левый потомок
        }
        if (item.getRight() != null) {
            this.printTree(level +1,item.getRight());  // Правый потомок
        }
    }
}
