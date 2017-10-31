package zjp.translateit.dto;

public class BackupResult {

    private int deleteCount;
    private int addCount;
    private int updateCount;

    public BackupResult() {
    }

    public BackupResult(int deleteCount, int addCount, int updateCount) {
        this.deleteCount = deleteCount;
        this.addCount = addCount;
        this.updateCount = updateCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int addCount) {
        this.addCount = addCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }
}
