package model;

// Класс - заглушка - требуется при удалении элемента дерева
public class NilItem extends Item {
    public NilItem() {
        super(0);
        this.setBlack(true);
    }
}
