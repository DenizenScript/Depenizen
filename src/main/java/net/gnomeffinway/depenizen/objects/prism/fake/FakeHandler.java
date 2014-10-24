package net.gnomeffinway.depenizen.objects.prism.fake;

import com.helion3.prism.libs.elixr.MaterialAliases;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actionlibs.QueryParameters;
import me.botsko.prism.actions.Handler;
import me.botsko.prism.appliers.ChangeResult;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FakeHandler implements Handler {

    private ActionType actionType;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private int blockId = -1;
    private int blockSubId = -1;
    private int oldBlockId = -1;
    private int oldBlockSubId = -1;
    private int aggregateCount = -1;
    private String playerName;

    public FakeHandler(ActionType actionType) {
        this.actionType = actionType;
    }
    @Override
    public String getUnixEpoch() {
        return null;
    }

    @Override
    public String getDisplayDate() {
        return null;
    }

    @Override
    public String getDisplayTime() {
        return null;
    }

    @Override
    public void setUnixEpoch(String s) {
    }

    @Override
    public String getTimeSince() {
        return null;
    }

    @Override
    public ActionType getType() {
        return actionType;
    }

    @Override
    public void setType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void setPlayerName(String s) {
        this.playerName = s;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void setBlockId(int i) {
        this.blockId = i;
    }

    @Override
    public void setBlockSubId(int i) {
        this.blockSubId = i;
    }

    @Override
    public int getBlockId() {
        return blockId;
    }

    @Override
    public int getBlockSubId() {
        return blockSubId;
    }

    @Override
    public void setOldBlockId(int i) {
        this.oldBlockId = i;
    }

    @Override
    public void setOldBlockSubId(int i) {
        this.oldBlockSubId = i;
    }

    @Override
    public int getOldBlockId() {
        return oldBlockId;
    }

    @Override
    public int getOldBlockSubId() {
        return oldBlockSubId;
    }

    @Override
    public void setAggregateCount(int i) {
        this.aggregateCount = i;
    }

    @Override
    public int getAggregateCount() {
        return aggregateCount;
    }

    @Override
    public String getNiceName() {
        return null;
    }

    @Override
    public String getData() {
        return null;
    }

    @Override
    public void setData(String s) {

    }

    @Override
    public void setMaterialAliases(MaterialAliases materialAliases) {

    }

    @Override
    public void save() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void setCanceled(boolean b) {

    }

    @Override
    public ChangeResult applyRollback(Player player, QueryParameters queryParameters, boolean b) {
        return null;
    }

    @Override
    public ChangeResult applyRestore(Player player, QueryParameters queryParameters, boolean b) {
        return null;
    }

    @Override
    public ChangeResult applyUndo(Player player, QueryParameters queryParameters, boolean b) {
        return null;
    }

    @Override
    public ChangeResult applyDeferred(Player player, QueryParameters queryParameters, boolean b) {
        return null;
    }

    @Override
    public void setPlugin(Plugin plugin) {
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void setId(int i) {
    }

}
