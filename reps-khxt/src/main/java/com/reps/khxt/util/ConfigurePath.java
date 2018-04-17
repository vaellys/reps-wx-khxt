package com.reps.khxt.util;

import com.reps.core.RepsContext;

public class ConfigurePath {

	/**附件上传路径*/
	public static final String ATTACHMENT_UPLOAD_PATH = RepsContext.getConst("khxt", "attachmentUploadPath");
	
	/**附件访问路径*/
	public static final String ATTACHMENT_FILE_PATH = RepsContext.getConst("khxt", "attachmentFilePath");
	
}
