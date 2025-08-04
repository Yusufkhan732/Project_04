package in.co.rays.bean;

import java.sql.Timestamp;

public abstract class BaseBean implements DropdownListBean {

	
	
	protected long id;
	protected String createdby;
	protected String modifiedby;
	protected Timestamp createdDatetime;
	protected Timestamp modifiedDatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdby;
	}

	public void setCreatedBy(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedBy() {
		return modifiedby;
	}

	public void setModifiedBy(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Timestamp getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Timestamp modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}
}
