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
        this.rbt.insertItem(40);  // Заполнение дерева данными
        this.rbt.insertItem(14);
        this.rbt.insertItem(86);
        this.rbt.insertItem(68);
        this.rbt.insertItem(83);
        this.rbt.insertItem(70);
        this.rbt.insertItem(15);
        this.rbt.insertItem(84);
        this.rbt.insertItem(47);

        this.printTree(0, this.rbt.getRoot());  // Визуализация дерева

        view.print("============= Удаление корня ============");
        this.rbt.deleteItem(this.rbt.getRoot().getData());   // Удаляем элемент (корень - так интереснее)
        this.printTree(0, this.rbt.getRoot());  // Визуализация дерева

        view.print("============= Конец работы ==============");
    }

    public void printTree(int level,Item item) {  // Рекурсивно выводим элементы дерева
        if (item != null) {
            view.print(String.format("Level: %d, %s", level, item));  // У item есть метод toString() - его не указываем
            if (item.getLeft() != null) {
                this.printTree(level + 1, item.getLeft());  // Левый потомок
            }
            if (item.getRight() != null) {
                this.printTree(level + 1, item.getRight());  // Правый потомок
            }
        }
    }
}
