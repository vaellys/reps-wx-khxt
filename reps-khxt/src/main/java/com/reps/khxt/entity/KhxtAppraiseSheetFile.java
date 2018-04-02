package com.reps.khxt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.reps.core.orm.IdEntity;

/**
 * 考核文件
 * 
 * @author ：Alex
 * @date 2018年3月22日
 */
@Entity
@Table(name = "reps_khxt_appraise_sheet_file")
public class KhxtAppraiseSheetFile extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6413638176663336741L;

	/**
	 * 考核表ID
	 */
	@Column(name = "performance_id", length = 32)
	private String performanceId;

	/**
	 * 考核表ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id",insertable=false,updatable=false)
	private KhxtAppraiseSheet AppraiseSheet;

	/**
	 * 文件名称
	 */
	@Column(name = "file_name", length = 60)
	private String fileName;
	/**
	 * 文件url
	 */
	@Column(name = "file_url", length = 120)
	private String fileUrl;
	/**
	 * 文件类型
	 */
	@Column(name = "file_type", length = 5)
	private String fileType;
	/**
	 * 文件大小
	 */
	@Column(name = "file_size")
	private Long fileSize;
	/**
	 * 上传时间
	 */
	@Column(name = "upload_time")
	private Date uploadTime;

	public String getPerformanceId() {
		return performanceId;
	}

	public void setPerformanceId(String performanceId) {
		this.performanceId = performanceId;
	}

	public KhxtAppraiseSheet getAppraiseSheet() {
		return AppraiseSheet;
	}

	public void setAppraiseSheet(KhxtAppraiseSheet appraiseSheet) {
		AppraiseSheet = appraiseSheet;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

}
