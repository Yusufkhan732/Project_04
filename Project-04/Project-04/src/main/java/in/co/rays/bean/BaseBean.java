package in.co.rays.bean;

import java.sql.Timestamp;

/**
 * Abstract base class for all beans containing common audit fields.
 * Provides ID, created/modified by, and created/modified timestamp fields.
 * 
 * @author Yusuf Khan
 */
public abstract class BaseBean implements DropdownListBean {


    /**
     * Unique identifier of the record.
     */
    protected long id;

    /**
     * Username of the creator of the record.
     */
    protected String createdby;

    /**
     * Username of the person who last modified the record.
     */
    protected String modifiedby;

    /**
     * Timestamp when the record was created.
     */
    protected Timestamp createdDatetime;

    /**
     * Timestamp when the record was last modified.
     */
    protected Timestamp modifiedDatetime;

    /**
     * Gets the ID of the record.
     * 
     * @return the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the record.
     * 
     * @param id the ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the username of the creator.
     * 
     * @return the creator's username
     */
    public String getCreatedBy() {
        return createdby;
    }

    /**
     * Sets the username of the creator.
     * 
     * @param createdby the creator's username to set
     */
    public void setCreatedBy(String createdby) {
        this.createdby = createdby;
    }

    /**
     * Gets the username of the last modifier.
     * 
     * @return the modifier's username
     */
    public String getModifiedBy() {
        return modifiedby;
    }

    /**
     * Sets the username of the last modifier.
     * 
     * @param modifiedby the modifier's username to set
     */
    public void setModifiedBy(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    /**
     * Gets the timestamp when the record was created.
     * 
     * @return the creation timestamp
     */
    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    /**
     * Sets the timestamp when the record was created.
     * 
     * @param createdDatetime the creation timestamp to set
     */
    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    /**
     * Gets the timestamp when the record was last modified.
     * 
     * @return the modification timestamp
     */
    public Timestamp getModifiedDatetime() {
        return modifiedDatetime;
    }

    /**
     * Sets the timestamp when the record was last modified.
     * 
     * @param modifiedDatetime the modification timestamp to set
     */
    public void setModifiedDatetime(Timestamp modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }
}