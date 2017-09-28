package com.global.elastic.enums;

/**
 * The Enum DataUploadTypeEnum.
 */
public enum DataUploadTypeEnum {

	/** The process. */
	PROCESS("process"), 
	
	/** The resource. */
	RESOURCE("resource");
	
	/** The upload type. */
	private String uploadType = null;
	
	/**
	 * Instantiates a new data upload type enum.
	 *
	 * @param uploadType the upload type
	 */
	private DataUploadTypeEnum(String uploadType){
		this.uploadType = uploadType;
	}
	
	/**
	 * Gets the upload type.
	 *
	 * @return the uploadType
	 */
	public String getUploadType() {
		return uploadType;
	}
	
	/**
	 * Sets the upload type.
	 *
	 * @param uploadType the uploadType to set
	 */
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	
	public static DataUploadTypeEnum getDataUploadTypeEnum(String uploadCategory){
		DataUploadTypeEnum dataUploadEnum = null;
		for(DataUploadTypeEnum dataUpload : DataUploadTypeEnum.values()){
			if (dataUpload.getUploadType().equalsIgnoreCase(uploadCategory)){
				dataUploadEnum = dataUpload;	
			}
		}
		return dataUploadEnum;
	}
}
