package im.nll.data.extractor.entity;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/8/4 上午10:20
 */
public class Config {
    // common options:

    private String encoding;
    // consumer options

    private String pollInterval;

    private String createEvent;

    private String modifyEvent;

    private String deleteEvent;

    private String mode;

    private String sortby;

    private String sortorder;
    // producer options
    private String compressFile;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(String pollInterval) {
        this.pollInterval = pollInterval;
    }

    public String getCreateEvent() {
        return createEvent;
    }

    public void setCreateEvent(String createEvent) {
        this.createEvent = createEvent;
    }

    public String getModifyEvent() {
        return modifyEvent;
    }

    public void setModifyEvent(String modifyEvent) {
        this.modifyEvent = modifyEvent;
    }

    public String getDeleteEvent() {
        return deleteEvent;
    }

    public void setDeleteEvent(String deleteEvent) {
        this.deleteEvent = deleteEvent;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public String getCompressFile() {
        return compressFile;
    }

    public void setCompressFile(String compressFile) {
        this.compressFile = compressFile;
    }
}
