package com.reps.khxt.service.impl;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.exception.RepsException;
import com.reps.core.util.StringUtil;
import com.reps.khxt.entity.CellMergeRange;
import com.reps.khxt.entity.ItemWeight;
import com.reps.khxt.entity.KhxtAppraiseSheet;
import com.reps.khxt.entity.KhxtItem;
import com.reps.khxt.entity.KhxtLevelPerson;
import com.reps.khxt.entity.KhxtPerformanceMembers;
import com.reps.khxt.entity.KhxtPerformancePoint;
import com.reps.khxt.service.IKhxtAppraiseSheetService;
import com.reps.khxt.service.IKhxtLevelPersonService;
import com.reps.khxt.service.IKhxtPerformanceMembersService;
import com.reps.khxt.service.IStatService;
import com.reps.khxt.util.WeightUtil;

@Service
@Transactional
public class StatServiceImpl implements IStatService {

	@Autowired
	IKhxtPerformanceMembersService khxtPerformanceMembersService;
	
	@Autowired
	IKhxtAppraiseSheetService khxtAppraiseSheetService;
	
	@Autowired
	IKhxtLevelPersonService khxtLevelPersonService;

	@Override
	public List<Map<String, Object>> computeAssessItem(String sheetId) throws RepsException {
		List<Map<String, Object>> resultList = null;
		if (StringUtil.isNotBlank(sheetId)) {
			// 查询特定考核表的人员名单
			KhxtPerformanceMembers khxtPerformanceMembers = new KhxtPerformanceMembers();
			khxtPerformanceMembers.setSheetId(sheetId);
			// 按被考核人分组查询
			List<String> bkhrPersonIds = khxtPerformanceMembersService.findByGroup(khxtPerformanceMembers);
			if (null != bkhrPersonIds && !bkhrPersonIds.isEmpty()) {
				resultList = new ArrayList<>();
				for (int i = 0; i < bkhrPersonIds.size() ; i++) {
					// 查询该考核表的被考核人的所有考核人
					khxtPerformanceMembers.setBkhrPersonId(bkhrPersonIds.get(i));
					List<KhxtPerformanceMembers> memberList = khxtPerformanceMembersService.find(khxtPerformanceMembers, true);
					if (null != memberList && !memberList.isEmpty()) {
						//存储每个按被考核人分组的信息
						Map<String, Object> resultMap = new HashMap<>();
						resultMap.put("bkhrPersonName", memberList.get(0).getBkhrPerson().getName());
						// 存储考核指标信息
						List<Map<String, Object>> itemList = new ArrayList<>();
						//收集所有的item对应的指标分数信息
						List<KhxtPerformancePoint> list = new ArrayList<>();
						//存储同级别的已打分的考核人级别和人数
						for (KhxtPerformanceMembers member : memberList) {
							Map<String, Object> memberMap = new HashMap<>();
							//获取到人员所对应的级别
							KhxtLevelPerson levelPerson = khxtLevelPersonService.getByPersonId(member.getKhrPerson().getId());
							String levelId = levelPerson.getLevelId();
							//查询该级别所对应的权重
							String weight = WeightUtil.findWeightByLevelId(levelId, member.getAppraiseSheet().getLevelWeight().getWeight());
							memberMap.put("khrPersonName", setBkhrPersonName(member.getKhrPerson().getName(), weight));
							List<KhxtPerformancePoint> performancePoints = member.getPerformancePoints();
							list.addAll(performancePoints);
							memberMap.put("itemPoints", performancePoints);
							memberMap.put("sumOfPerRow", member.getTotalPoints());
							itemList.add(memberMap);
						}
						resultMap.put("itemList", itemList);
						
						double sum = 0.0;
						//存储每种指标的权重计算之后
						List<ItemWeight> itemWeightList = new ArrayList<>();
						//对按被考核人进行分组之后 的每组 按指标进行分组
						Map<String, List<KhxtPerformancePoint>> pointMap = list.stream().collect(groupingBy(KhxtPerformancePoint::getItemId));
						for (Map.Entry<String, List<KhxtPerformancePoint>> entry : pointMap.entrySet()) {
							//按人员级别对人员评分进行分组
							Map<String, List<KhxtPerformancePoint>> levelPointMap = new HashMap<>();
							List<KhxtPerformancePoint> targetList = null;
							for (KhxtPerformancePoint p : entry.getValue()) {
								KhxtLevelPerson levelPerson = khxtLevelPersonService.getByPersonId(p.getKhxtPerformanceMembers().getKhrPerson().getId());
								String levelId = levelPerson.getLevelId();
								if(levelPointMap.containsKey(levelId)) {
									levelPointMap.get(levelId).add(p);
								} else {
									targetList = new ArrayList<>();
									targetList.add(p);
									levelPointMap.put(levelId, targetList);
								}
							}
							//对得分进行计算
							List<Double> sumList = new ArrayList<>();
							for (Map.Entry<String, List<KhxtPerformancePoint>> lpm : levelPointMap.entrySet()) {
								String levelId = lpm.getKey();
								Double tempSum = 0.0;
								for (KhxtPerformancePoint pp : lpm.getValue()) {
									Double point = pp.getPoint();
									Double weight = Double.valueOf(WeightUtil.findWeightByLevelId(levelId, pp.getKhxtPerformanceMembers().getAppraiseSheet().getLevelWeight().getWeight()));
									tempSum += weight / 100 * point;
								}
								tempSum = (tempSum / lpm.getValue().size());
								sumList.add(tempSum);
							}
							itemWeightList.add(new ItemWeight(sumList.stream().collect(Collectors.summingDouble(point -> {
								double count = 0.0;
								count += point;
								return count;})), entry.getKey()));
						}
						//计算汇总
						sum = itemWeightList.stream().collect(Collectors.summingDouble(itemWeight -> {
							double sumOfAllItem = 0.0;
							sumOfAllItem += itemWeight.getPoint();
							return sumOfAllItem;
						}));
						resultMap.put("sum", sum);
						resultMap.put("itemWeightList", itemWeightList);
						resultList.add(resultMap);
					}
				}
			}
		}
		return resultList;
	}
	
	private String setBkhrPersonName(String name, String weight) {
		StringBuilder sb = new StringBuilder();
		if(StringUtil.isNotBlank(name) && StringUtil.isNotBlank(weight)) {
			sb.append(name);
			sb.append("（");
			sb.append(weight);
			sb.append("%）");
		}
		return sb.toString();
	}
	
	@Override
	public Map<String, Object> buildExcelDatas(String sheetId) throws RepsException {
		Map<String, Object> datas = null;
		List<Map<String, Object>> resultList = null;
		//合并单元格的起始和结束偏移量
		List<CellMergeRange> cellRangeList = null;
		if (StringUtil.isNotBlank(sheetId)) {
			datas = new HashMap<>();
			KhxtAppraiseSheet khxtAppraiseSheet = khxtAppraiseSheetService.get(sheetId, true);
			Set<KhxtItem> itemList = khxtAppraiseSheet.getItem();
			// 查询特定考核表的人员名单
			KhxtPerformanceMembers khxtPerformanceMembers = new KhxtPerformanceMembers();
			khxtPerformanceMembers.setSheetId(sheetId);
			// 按被考核人分组查询
			List<String> bkhrPersonIds = khxtPerformanceMembersService.findByGroup(khxtPerformanceMembers);
			if (null != bkhrPersonIds && !bkhrPersonIds.isEmpty()) {
				resultList = new ArrayList<>();
				cellRangeList = new ArrayList<>();
				int index = 1;
				for (int i = 0; i < bkhrPersonIds.size() ; i++) {
					// 查询该考核表的被考核人的所有考核人
					khxtPerformanceMembers.setBkhrPersonId(bkhrPersonIds.get(i));
					List<KhxtPerformanceMembers> memberList = khxtPerformanceMembersService.find(khxtPerformanceMembers, true);
					if (null != memberList && !memberList.isEmpty()) {
						int size = memberList.size();
						CellMergeRange cellMergeRange = new CellMergeRange(index, index + size);
						index += size + 1;
						cellRangeList.add(cellMergeRange);
						
						//收集所有的item对应的指标分数信息
						List<KhxtPerformancePoint> list = new ArrayList<>();
						int num = 0;
						String name = "";
						for (KhxtPerformanceMembers member : memberList) {
							Map<String, Object> memberMap = new LinkedHashMap<>();
							num =  i + 1;
							memberMap.put("serial", i + 1);
							name = member.getBkhrPerson().getName();
							memberMap.put("bkhrPersonName", name);
							KhxtLevelPerson levelPerson = khxtLevelPersonService.getByPersonId(member.getKhrPerson().getId());
							String weight = WeightUtil.findWeightByLevelId(levelPerson.getLevelId(), member.getAppraiseSheet().getLevelWeight().getWeight());
							memberMap.put("khrPersonName", setBkhrPersonName(member.getKhrPerson().getName(), weight));
							//增加指标分数
							List<KhxtPerformancePoint> performancePoints = member.getPerformancePoints();
							for (KhxtItem item : itemList) {
								for (KhxtPerformancePoint khxtPerformancePoint : performancePoints) {
									if(item.getId().equals(khxtPerformancePoint.getItemId())) {
										memberMap.put(item.getName(), khxtPerformancePoint.getPoint());
										break;
									}
								}
							}
							list.addAll(performancePoints);
							//增加汇总
							memberMap.put("totalPoints", member.getTotalPoints());
							resultList.add(memberMap);
						}
						
						//增加按被考核人分组之后的每组小计及汇总
						double sum = 0.0;
						//存储每种指标的权重计算之后
						List<ItemWeight> itemWeightList = new ArrayList<>();
						//对按被考核人进行分组之后 的每组 按指标进行分组
						Map<String, List<KhxtPerformancePoint>> pointMap = list.stream().collect(groupingBy(KhxtPerformancePoint::getItemId));
						for (Map.Entry<String, List<KhxtPerformancePoint>> entry : pointMap.entrySet()) {
							//按人员级别对人员评分进行分组
							Map<String, List<KhxtPerformancePoint>> levelPointMap = new HashMap<>();
							List<KhxtPerformancePoint> targetList = null;
							for (KhxtPerformancePoint p : entry.getValue()) {
								KhxtLevelPerson levelPerson = khxtLevelPersonService.getByPersonId(p.getKhxtPerformanceMembers().getKhrPerson().getId());
								String levelId = levelPerson.getLevelId();
								if(levelPointMap.containsKey(levelId)) {
									levelPointMap.get(levelId).add(p);
								} else {
									targetList = new ArrayList<>();
									targetList.add(p);
									levelPointMap.put(levelId, targetList);
								}
							}
							//对得分进行计算
							List<Double> sumList = new ArrayList<>();
							for (Map.Entry<String, List<KhxtPerformancePoint>> lpm : levelPointMap.entrySet()) {
								String levelId = lpm.getKey();
								Double tempSum = 0.0;
								for (KhxtPerformancePoint pp : lpm.getValue()) {
									Double point = pp.getPoint();
									Double weight = Double.valueOf(WeightUtil.findWeightByLevelId(levelId, pp.getKhxtPerformanceMembers().getAppraiseSheet().getLevelWeight().getWeight()));
									tempSum += weight / 100 * point;
								}
								tempSum = (tempSum / lpm.getValue().size());
								sumList.add(tempSum);
							}
							itemWeightList.add(new ItemWeight(sumList.stream().collect(Collectors.summingDouble(point -> {
								double count = 0.0;
								count += point;
								return count;})), entry.getKey()));
						}
						//计算汇总
						sum = itemWeightList.stream().collect(Collectors.summingDouble(itemWeight -> {
							double sumOfAllItem = 0.0;
							sumOfAllItem += itemWeight.getPoint();
							return sumOfAllItem;
						}));
						
						Map<String, Object> memberMap = new LinkedHashMap<>();
						memberMap.put("serial", num);
						memberMap.put("bkhrPersonName", name);
						memberMap.put("khrPersonName", "");
						
						for (KhxtItem item : itemList) {
							for (ItemWeight weight : itemWeightList) {
								if(item.getId().equals(weight.getId())) {
									memberMap.put(item.getName(), weight.getPoint());
									break;
								}
							}
						}
						
						//增加汇总
						memberMap.put("sum", sum);
						resultList.add(memberMap);
					}
				}
			}
			datas.put("result", resultList);
			datas.put("cellRanges", cellRangeList);
		}
		return datas;
	}

}
