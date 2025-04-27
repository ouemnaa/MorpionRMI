package shared;
import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private boolean wantsRematch;
    
    public Player(String name) {
        this.name = name;
        this.wantsRematch = false;
    }
    
    public String getName() { return name; }
    public boolean wantsRematch() { return wantsRematch; }
    public void setRematch(boolean rematch) { wantsRematch = rematch; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return name.equals(other.name);
    }
}