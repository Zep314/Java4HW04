package model;

import java.awt.*;

public class Item {
    private int data;
    private boolean is_black;
    private Item left;
    private Item right;
    private Item parent;

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
    }

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
    public String toString() {
        return String.format("Item: %d, Color: %s, Parent: %s, Left: %s, Right: %s"
                , this.getData()
                , (this.is_black()) ? "Black" : "Red"
                , (this.getParent() != null) ? this.getParent().getData() : "null"
                , (this.getLeft() != null) ? this.getLeft().getData() : "null"
                , (this.getRight() != null) ? this.getRight().getData() : "null"
                );
    }
}
