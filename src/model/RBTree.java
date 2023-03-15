package model;

// Класс левостороннего красно-черного дерева элементов
public class RBTree {
    private Item root;  // Корень дерева

    public RBTree() {
        root = null;
    }

    public Item getRoot() {
        return this.root;
    }
    private void rotateRight(Item item) {  // Правый поворот
        Item parent = item.getParent();
        Item leftChild = item.getLeft();

        item.setLeft(leftChild.getRight());
        if (leftChild.getRight() != null) {
            leftChild.getRight().setParent(item);
        }
        leftChild.setRight(item);
        item.setParent(leftChild);
        this.replaceParentsChild(parent, item, leftChild);  // исправляем связи между повернутыми узлами
    }

    private void rotateLeft(Item item) {  // Левый поворот
        Item parent = item.getParent();
        Item rightChild = item.getRight();

        item.setRight(rightChild.getLeft());
        if (rightChild.getLeft() != null) {
            rightChild.getLeft().setParent(item);
        }
        rightChild.setLeft(item);
        item.setParent(rightChild);
        this.replaceParentsChild(parent, item, rightChild);  // исправляем связи между повернутыми узлами
    }

    // исправляем связи между повернутыми узлами
    private void replaceParentsChild(Item parent, Item oldChild, Item newChild) {
        if (parent == null) {
            this.root = newChild;
        } else if (parent.getLeft() == oldChild) {
            parent.setLeft(newChild);
        } else if (parent.getRight() == oldChild) {
            parent.setRight(newChild);
        } else {
            // Узел не наследник своего родителя
            throw new IllegalStateException("Item is not a child of its parent");
        }
        if (newChild != null) {
            newChild.setParent(parent);
        }
    }

    @SuppressWarnings("unused")
    public Item searchItem(int key) {  // Поиск элемента
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

    public void insertItem(int key) {  // Вставка нового элемента
        Item item = this.root;
        Item parent = null;

        // Перемещаемся по дереву, ищем подходящий элемент
        while (item != null) {
            parent = item;
            if (key < item.getData()) {
                item = item.getLeft();
            } else if (key > item.getData()) {
                item = item.getRight();
            } else {
                // Ошибка! Элемент с таким ключом уже есть в дереве
                throw new IllegalArgumentException("Tree already contains item with key " + key);
            }
        }

        // Вставка нового элемента
        Item newItem = new Item(key);
        newItem.setBlack(false);  // Новый элемент - всегда красный (кроме самого первого)
        if (parent == null) {
            this.root = newItem;  // Первый элемент
        } else if (key < parent.getData()) {  // Настройка элемента - родителя
            parent.setLeft(newItem);
        } else {
            parent.setRight(newItem);
        }
        newItem.setParent(parent);

        this.rbTreeBalancing(newItem); // Восстанавливаем правила (балансировка) красно-черного дерева,
                                       // которые могли нарушиться после вставки нового элемента
    }

    private void rbTreeBalancing(Item item) {  // Балансировка дерева
        Item parent = item.getParent();

        // Родитель - пуст, это корень дерева - балансировать нечего
        if (parent == null) return;

        // Родитель - черный - балансировать нечего
        if (parent.is_black()) return;

        // Тут родитель всегда красный
        Item grandparent = parent.getParent(); // поднимаемся на 2 уровня выше

        // Отсутствие элемента grandparent - означает, что parent - это корень дерева
        if (grandparent == null) {
            // Корень - всегда черный
            parent.setBlack(true);
            return;
        }

        // Соседний узел, рядом с родительским
        Item uncle = this.getUncle(parent);

        // Uncle существует - и он - красный -> перекрашиваем родителя, grandparent и uncle
        if (uncle != null && !uncle.is_black()) {
            parent.setBlack(true);
            grandparent.setBlack(false);
            uncle.setBlack(true);

            // Вызываем рекурсивно для grandparent, который сейчас красный.
            rbTreeBalancing(grandparent);
        }

        // Родитель - левый потомок grandparent
        else {
            if (parent == grandparent.getLeft()) {
                // Uncle - черный и элемент "внутренний внук"
                if (item == parent.getRight()) {
                    this.rotateLeft(parent);  // вращаем влево
                    parent = item;
                }

                // Uncle - черный и элемент "внешний внук"
                this.rotateRight(grandparent);
            }

            // Родитель - правый потомок grandparent
            else {
                // Uncle - черный и элемент "внутренний внук"
                if (item == parent.getLeft()) {
                    this.rotateRight(parent);
                    parent = item;
                }

                // Uncle - черный и элемент "внешний внук"
                this.rotateLeft(grandparent);
            }
            // Перекрашиваем родителя и grandparent
            parent.setBlack(true);
            grandparent.setBlack(false);
        }
    }

    // Возвращаем соседний узел, рядом с родительским
    private Item getUncle(Item parent) {
        Item grandparent = parent.getParent();  // На 2 уровня вверх
        if (grandparent.getLeft() == parent) {  // Выбираем нужный узл (левый или правый)
            return grandparent.getRight();
        } else if (grandparent.getRight() == parent) {
            return grandparent.getLeft();
        } else {  // Что-то пошло не так
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    // Удаление элемента
    public void deleteItem(int key) {
        Item item = this.root;

        // Ищем элемент для удаления
        while (item != null && item.getData() != key) {
            // Опускаемся вниз по дереву
            if (key < item.getData()) {
                item = item.getLeft();
            } else {
                item = item.getRight();
            }
        }

        // Узел не найден!
        if (item == null) {
            return;
        }

        // item - это то, что надо удалить

        // Тут храним элемент, от которого будем "чинить" дерево после удаления
        Item movedUpItem;
        boolean deletedItemColor;

        // У элемента один, или ни одного потомка
        if (item.getLeft() == null || item.getRight() == null) {
            movedUpItem = this.deleteItemWithZeroOrOneChild(item);
            deletedItemColor = item.is_black();
        }

        // У элемента 2 потомка
        else {
            // Ищем минимальный элемент поддерева справа ("правопереемник" текущего элемента)
            Item inOrderSuccessor = this.findMinimum(item.getRight());

            // Копируем данные переемника в текущий элемент
            item.setData(inOrderSuccessor.getData());

            // Удаляем элемент
            movedUpItem = deleteItemWithZeroOrOneChild(inOrderSuccessor);
            deletedItemColor = inOrderSuccessor.is_black();
        }

        if (deletedItemColor) {  // "Чиним" дерево
            this.fixRedBlackPropertiesAfterDelete(movedUpItem);

            // Удаляем пустой элемент
            if (movedUpItem.getClass() == NilItem.class) {
                this.replaceParentsChild(movedUpItem.getParent(), movedUpItem, null);
            }
        }
    }

    private Item deleteItemWithZeroOrOneChild(Item item) {
        // Элемент имеет только левого потомка --> заменяем его левым потомком
        if (item.getLeft() != null) {
            this.replaceParentsChild(item.getParent(), item, item.getLeft());
            return item.getLeft(); // удаляемый элемент
        }

        // Элемент имеет только правого потомка --> заменяем его правым потомком
        else if (item.getRight() != null) {
            replaceParentsChild(item.getParent(), item, item.getRight());
            return item.getRight(); // удаляемый элемент
        }

        // У элемента нет потомков
        // * Элемент красный --> просто удаляем его
        // * Элемент черный --> временно заменяем его нулевым элементом (для восстановления правил)
        else {
            Item newChild = item.is_black() ? new NilItem() : null;
            this.replaceParentsChild(item.getParent(), item, newChild);
            return newChild;
        }
    }

    // Ищем последовательного преемника поддерева
    private Item findMinimum(Item item) {
        while (item.getLeft() != null) {
            item = item.getLeft();
        }
        return item;
    }

    // Восстанавливаем дерево (правила) после удаления элемента
    private void fixRedBlackPropertiesAfterDelete(Item item) {
        // Если элемент - корень, то заканчиваем обработку
        if (item == this.root) {
            return;
        }

        Item sibling = getSibling(item);  // "Братский" элемент (когда родитель один и тот же)

        // Брат - красный
        if (!sibling.is_black()) {
            handleRedSibling(item, sibling);
            sibling = getSibling(item);
        }

        // Черный брат и два его черных потомка
        if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {
            sibling.setBlack(false);

            // Черный брат и два его черных потомка и красный родитель
            if (!item.getParent().is_black()) {
                item.getParent().setBlack(true);
            }

            // Черный брат и два его черных потомка и черный родитель
            else {
                fixRedBlackPropertiesAfterDelete(item.getParent());
            }
        }

        // Черный брат с хотя бы одним красным потомком
        else {
            this.handleBlackSiblingWithAtLeastOneRedChild(item, sibling);
        }
    }

    // Возвращение "братского" элемента (когда один и тот же родитель
    private Item getSibling(Item item) {
        Item parent = item.getParent();
        if (item == parent.getLeft()) {
            return parent.getRight();
        } else if (item == parent.getRight()) {
            return parent.getLeft();
        } else {  // Что-то пошло не так
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    private boolean isBlack(Item item) {
        return item == null || item.is_black();
    }

    // Обработка красного брата-соседа
    private void handleRedSibling(Item item, Item sibling) {
        // Перекраска
        sibling.setBlack(true);
        item.getParent().setBlack(false);

        // вращение
        if (item == item.getParent().getLeft()) {
            this.rotateLeft(item.getParent());
        } else {
            this.rotateRight(item.getParent());
        }
    }

    // Обработка черного "соседа" с хотя бы одним красным потомком
    private void handleBlackSiblingWithAtLeastOneRedChild(Item item, Item sibling) {
        boolean nodeIsLeftChild = item == item.getParent().getLeft();

        // Черный брат с хотя бы одним красным ребенком + "внешний племянник" черный
        // Нужно перекрасить одноуровневый и его дочерний элементы и повернуть
        // вокруг соседнего элемента на уровне
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

        // Черный брат с хотя бы одним красным ребенком + "внешний племянник" красный
        // Нужно перекрасить одноуровневый элемент + родительский элемент + дочерний элемент
        // одноуровневого элемента и повернуть вокруг родителя
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
