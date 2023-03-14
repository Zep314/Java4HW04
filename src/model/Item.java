package model;

// Класс, описывает один элемент красно-черного дерева
public class Item {
    private int data;  // Данные (они же и ключ)
    private boolean is_black;  // Цвет - черный/не черный
    private Item left;  // Ссылка на левый элемент - потомок
    private Item right;  // Ссылка на правый элемент - потомок
    private Item parent;  // Ссылка на предыдущий (родительский) элемент

    @SuppressWarnings("unused")
    public Item() {
        this.data = 0;
        this.is_black = true;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Item(int number) {
        this.data = number;
        this.is_black = true;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    // геттеры - сеттеры
    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public boolean is_black() {
        return this.is_black;
    }

    public void setBlack(boolean black) {
        this.is_black = black;
    }

    public Item getLeft() {
        return this.left;
    }
    public Item getRight() {
        return this.right;
    }
    public Item getParent() {
        return this.parent;
    }
    public void setLeft(Item item) {
        this.left = item;
    }
    public void setRight(Item item) {
        this.right = item;
    }
    public void setParent(Item item) {
        this.parent = item;
    }

    @Override
    public String toString() {  // Красиво компонуем строку с данными элемента
        return String.format("Item: %d, Color: %s, Parent: %s, Left: %s, Right: %s"
                , this.getData()
                , (this.is_black()) ? "Black" : "Red"
                , (this.getParent() != null) ? this.getParent().getData() : "null"
                , (this.getLeft() != null) ? this.getLeft().getData() : "null"
                , (this.getRight() != null) ? this.getRight().getData() : "null"
                );
    }
}
