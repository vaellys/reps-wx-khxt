package com.reps.khxt.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.DateUtil;
import com.reps.core.util.StringUtil;
import com.reps.khxt.dao.KhxtKhrProcessDao;
import com.reps.khxt.entity.KhxtKhrProcess;
import com.reps.khxt.enums.MarkStatus;
import com.reps.khxt.service.IKhxtKhrProcessService;
import com.reps.khxt.service.IKhxtLevelPersonService;

@Service
@Transactional
public class KhxtKhrProcessServiceImpl implements IKhxtKhrProcessService {
	
	protected final Logger logger = LoggerFactory.getLogger(KhxtKhrProcessServiceImpl.class);
	
	@Autowired
	KhxtKhrProcessDao dao;
	
	@Autowired
	private IKhxtLevelPersonService levelPersonService;

	@Override
	public List<KhxtKhrProcess> find(KhxtKhrProcess khxtKhrProcess) throws RepsException {
		return dao.find(khxtKhrProcess);
	}
	
	@Override
	public ListResult<KhxtKhrProcess> query(int start, int pagesize, KhxtKhrProcess process) throws RepsException {
		process.setBeginDate(formatDateStr(process.getBeginDate()));
		process.setEndDate(formatDateStr(process.getEndDate()));
		ListResult<KhxtKhrProcess> listResult = dao.query(start, pagesize, process);
		if(null != listResult) {
			for (KhxtKhrProcess bean : listResult.getList()) {
				bean.setOrganizeName(levelPersonService.getByPersonId(bean.getKhrPersonId()).getOrganize().getName());
				bean.getAppraiseSheet().setCheckCompletedMarking(this.checkCompletedMarking(bean.getSheetId(), bean.getKhrPersonId()));
			}
		}
		return listResult;
	}
	
	private String formatDateStr(String dateStr) {
		if(StringUtil.isNotBlank(dateStr)) {
			return DateUtil.formatStrDateTime(dateStr, "yyyy年MM月dd日", "yyyyMMdd");
		}
		return dateStr;
	}
	
	@Override
	public boolean checkKhr(String sheetId, String personId) throws RepsException {
		if(StringUtil.isBlank(sheetId)) {
			throw new RepsException("参数异常:考核表ID为空");
		}
		if(StringUtil.isNotBlank(personId)) {
			KhxtKhrProcess khxtKhrProcess = new KhxtKhrProcess();
			khxtKhrProcess.setKhrPersonId(personId);
			khxtKhrProcess.setSheetId(sheetId);
			List<KhxtKhrProcess> list = dao.find(khxtKhrProcess);
			if(null != list && !list.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean checkCompletedMarking(String sheetId, String personId)  throws RepsException {
		if(StringUtil.isBlank(sheetId)) {
			throw new RepsException("参数异常:考核表ID为空");
		}
		if(StringUtil.isNotBlank(personId)) {
			KhxtKhrProcess khxtKhrProcess = new KhxtKhrProcess();
			khxtKhrProcess.setKhrPersonId(personId);
			khxtKhrProcess.setSheetId(sheetId);
			List<KhxtKhrProcess> list = dao.find(khxtKhrProcess);
			if(null != list && !list.isEmpty()) {
				KhxtKhrProcess process = list.get(0);
				if(MarkStatus.FINISHED_MARKING.getId().intValue() == process.getStatus().intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void updateStatus(KhxtKhrProcess khxtKhrProcess) throws RepsException {
		if(null == khxtKhrProcess) {
			throw new RepsException("参数异常");
		}
		Integer status = khxtKhrProcess.getStatus();
		if(null == status) {
			throw new RepsException("参数异常:打分情况状态为空");
		}
		String sheetId = khxtKhrProcess.getSheetId();
		if(StringUtil.isBlank(sheetId)) {
			throw new RepsException("参数异常:考核表ID为空");
		}
		String khrPersonId = khxtKhrProcess.getKhrPersonId();
		if(StringUtil.isBlank(khrPersonId)) {
			throw new RepsException("参数异常:考核人ID为空");
		}
		dao.updateStatus(khxtKhrProcess);
	}

}
