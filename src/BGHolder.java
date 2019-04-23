
import javafx.scene.layout.Background;

/**
 * Holds an Background
 * @author rvenezia
 */
public class BGHolder {
    private Background bg;
    private String name;
    
    public BGHolder(Background bg, String name)
    {
        this.bg = bg;
        this.name = name;
    }
    
    public Background getBG()
    {
        return bg;
    }
    
    public String toString()
    {
        return name;
    }
}
