package model;

public class RBTree {
    private Item root;

    public RBTree() {
        root = null;
    }

//    public void add(Item item) {
//        if (root == null) {
//            root = item;
//        }
//    }
//
//    public void add(int number) {
//        Item item = new Item(number);
//    }

    public Item getRoot() {
        return this.root;
    }
    private void rotateRight(Item item) {
        Item parent = item.getParent();
        Item leftChild = item.getLeft();

        item.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(item);
        }
        leftChild.setRight(item);
        item.setParent(leftChild);
        this.replaceParentsChild(parent, item, leftChild);
    }

    private void rotateLeft(Item item) {
        Item parent = item.getParent();
        Item rightChild = item.getRight();

        item.setRight(rightChild.getLeft());
        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(item);
        }
        rightChild.setLeft(item);
        item.setParent(rightChild);
        this.replaceParentsChild(parent, item, rightChild);
    }

    private void replaceParentsChild(Item parent, Item oldChild, Item newChild) {
        if (parent == null) {
            this.root = newChild;
        } else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        } else if (parent.getRight() == oldChild) {
            parent.setRight(newChild);
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }
        if (newChild != null) {
            newChild.setParent(parent);
        }
    }

    public Item searchItem(int key) {
        Item item = this.root;
        while (item != null) {
            if (key == item.getData()) {
                return item;
            } else if (key < item.getData()) {
                item = item.getLeft();
            } else {
                item = item.getRight();
            }
        }
        return null;
    }

    public void insertItem(int key) {
        Item item = this.root;
        Item parent = null;

        // Traverse the tree to the left or right depending on the key
        while (item != null) {
            parent = item;
            if (key < item.getData()) {
                item = item.getLeft();
            } else if (key > item.getData()) {
                item = item.getRight();
            } else {
                throw new IllegalArgumentException("BST already contains a node with key " + key);
            }
        }

        // Insert new node
        Item newItem = new Item(key);
        newItem.setBlack(false);  // RED!
        if (parent == null) {
            this.root = newItem;
        } else if (key < parent.getData()) {
            parent.setLeft(newItem);
        } else {
            parent.setRight(newItem);
        }
        newItem.setParent(parent);

        this.fixRedBlackPropertiesAfterInsert(newItem);
    }



    private void fixRedBlackPropertiesAfterInsert(Item item) {
        Item parent = item.getParent();

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        // Parent is black --> nothing to do
        if (parent.is_black()) {
            return;
        }

        // From here on, parent is red
        Item grandparent = parent.getParent();

        // Case 2:
        // Not having a grandparent means that parent is the root. If we enforce black roots
        // (rule 2), grandparent will never be null, and the following if-then block can be
        // removed.
        if (grandparent == null) {
            // As this method is only called on red nodes (either on newly inserted ones - or -
            // recursively on red grandparents), all we have to do is to recolor the root black.
            parent.setBlack(true);
            return;
        }

        // Get the uncle (may be null/nil, in which case its color is BLACK)
        Item uncle = this.getUncle(parent);

        // Case 3: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && !uncle.is_black()) {
            parent.setBlack(true);
            grandparent.setBlack(false);
            uncle.setBlack(true);

            // Call recursively for grandparent, which is now red.
            // It might be root or have a red parent, in which case we need to fix more...
            fixRedBlackPropertiesAfterInsert(grandparent);
        }

        // Parent is left child of grandparent
        else if (parent == grandparent.getLeft()) {
            // Case 4a: Uncle is black and node is left->right "inner child" of its grandparent
            if (item == parent.getRight()) {
                this.rotateLeft(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = item;
            }

            // Case 5a: Uncle is black and node is left->left "outer child" of its grandparent
            this.rotateRight(grandparent);

            // Recolor original parent and grandparent
            parent.setBlack(true);
            grandparent.setBlack(false);
        }

        // Parent is right child of grandparent
        else {
            // Case 4b: Uncle is black and node is right->left "inner child" of its grandparent
            if (item == parent.getLeft()) {
                this.rotateRight(parent);

                // Let "parent" point to the new root node of the rotated sub-tree.
                // It will be recolored in the next step, which we're going to fall-through to.
                parent = item;
            }

            // Case 5b: Uncle is black and node is right->right "outer child" of its grandparent
            this.rotateLeft(grandparent);

            // Recolor original parent and grandparent
            parent.setBlack(true);
            grandparent.setBlack(false);
        }
    }

    private Item getUncle(Item parent) {
        Item grandparent = parent.getParent();
        if (grandparent.getLeft() == parent) {
            return grandparent.getRight();
        } else if (grandparent.getRight() == parent) {
            return grandparent.getLeft();
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }


    public void deleteItem(int key) {
        Item item = this.root;

        // Find the node to be deleted
        while (item != null && item.getData() != key) {
            // Traverse the tree to the left or right depending on the key
            if (key < item.getData()) {
                item = item.getLeft();
            } else {
                item = item.getRight();
            }
        }

        // Node not found?
        if (item == null) {
            return;
        }

        // At this point, "node" is the node to be deleted

        // In this variable, we'll store the node at which we're going to start to fix the R-B
        // properties after deleting a node.
        Item movedUpItem;
        boolean deletedItemColor;

        // Node has zero or one child
        if (item.getLeft() == null || item.getRight() == null) {
            movedUpItem = this.deleteItemWithZeroOrOneChild(item);
            deletedItemColor = item.is_black();
        }

        // Node has two children
        else {
            // Find minimum node of right subtree ("inorder successor" of current node)
            Item inOrderSuccessor = this.findMinimum(item.getRight());

            // Copy inorder successor's data to current node (keep its color!)
            item.setData(inOrderSuccessor.getData());

            // Delete inorder successor just as we would delete a node with 0 or 1 child
            movedUpItem = deleteItemWithZeroOrOneChild(inOrderSuccessor);
            deletedItemColor = inOrderSuccessor.is_black();
        }

        if (deletedItemColor) {
            this.fixRedBlackPropertiesAfterDelete(movedUpItem);

            // Remove the temporary NIL node
            if (movedUpItem.getClass() == NilItem.class) {
                this.replaceParentsChild(movedUpItem.getParent(), movedUpItem, null);
            }
        }
    }

    private Item deleteItemWithZeroOrOneChild(Item item) {
        // Node has ONLY a left child --> replace by its left child
        if (item.getLeft() != null) {
            this.replaceParentsChild(item.getParent(), item, item.getLeft());
            return item.getLeft(); // moved-up node
        }

        // Node has ONLY a right child --> replace by its right child
        else if (item.getRight() != null) {
            replaceParentsChild(item.getParent(), item, item.getRight());
            return item.getRight(); // moved-up node
        }

        // Node has no children -->
        // * node is red --> just remove it
        // * node is black --> replace it by a temporary NIL node (needed to fix the R-B rules)
        else {
            Item newChild = item.is_black() ? new NilItem() : null;
            this.replaceParentsChild(item.getParent(), item, newChild);
            return newChild;
        }
    }

    private Item findMinimum(Item item) {
        while (item.getLeft() != null) {
            item = item.getLeft();
        }
        return item;
    }




    private void fixRedBlackPropertiesAfterDelete(Item item) {
        // Case 1: Examined node is root, end of recursion
        if (item == this.root) {
            // Uncomment the following line if you want to enforce black roots (rule 2):
            // node.color = BLACK;
            return;
        }

        Item sibling = getSibling(item);

        // Case 2: Red sibling
        if (!sibling.is_black()) {
            handleRedSibling(item, sibling);
            sibling = getSibling(item); // Get new sibling for fall-through to cases 3-6
        }

        // Cases 3+4: Black sibling with two black children
        if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {
            sibling.setBlack(false);

            // Case 3: Black sibling with two black children + red parent
            if (!item.getParent().is_black()) {
                item.getParent().setBlack(true);
            }

            // Case 4: Black sibling with two black children + black parent
            else {
                fixRedBlackPropertiesAfterDelete(item.getParent());
            }
        }

        // Case 5+6: Black sibling with at least one red child
        else {
            this.handleBlackSiblingWithAtLeastOneRedChild(item, sibling);
        }
    }

    private Item getSibling(Item item) {
        Item parent = item.getParent();
        if (item == parent.getLeft()) {
            return parent.getRight();
        } else if (item == parent.getRight()) {
            return parent.getLeft();
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Item item) {
        return item == null || item.is_black();
    }

    private void handleRedSibling(Item item, Item sibling) {
        // Recolor...
        sibling.setBlack(true);
        item.getParent().setBlack(false);

        // ... and rotate
        if (item == item.getParent().getLeft()) {
            this.rotateLeft(item.getParent());
        } else {
            this.rotateRight(item.getParent());
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Item item, Item sibling) {
        boolean nodeIsLeftChild = item == item.getParent().getLeft();

        // Case 5: Black sibling with at least one red child + "outer nephew" is black
        // --> Recolor sibling and its child, and rotate around sibling
        if (nodeIsLeftChild && isBlack(sibling.getRight())) {
            sibling.getLeft().setBlack(true);
            sibling.setBlack(false);
            this.rotateRight(sibling);
            sibling = item.getParent().getRight();
        } else if (!nodeIsLeftChild && isBlack(sibling.getLeft())) {
            sibling.getRight().setBlack(true);
            sibling.setBlack(false);
            this.rotateLeft(sibling);
            sibling = item.getParent().getLeft();
        }

        // Fall-through to case 6...

        // Case 6: Black sibling with at least one red child + "outer nephew" is red
        // --> Recolor sibling + parent + sibling's child, and rotate around parent
        sibling.setBlack(item.getParent().is_black());
        item.getParent().setBlack(true);
        if (nodeIsLeftChild) {
            sibling.getRight().setBlack(true);
            this.rotateLeft(item.getParent());
        } else {
            sibling.getLeft().setBlack(true);
            this.rotateRight(item.getParent());
        }
    }
}
