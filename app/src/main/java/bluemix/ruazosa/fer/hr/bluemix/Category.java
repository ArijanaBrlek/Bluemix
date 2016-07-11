package bluemix.ruazosa.fer.hr.bluemix;

/**
 * Created by arijana on 7/3/16.
 */
public class Category {

    private CategoryItem[] items;

    public Category(CategoryItem ... items) {
        this.items = items;
    }

    public CategoryItem[] getItems() {
        return items;
    }
}
