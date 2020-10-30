package com.cqi.hr.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqi.hr.constant.Constant;
import com.cqi.hr.dao.AbstractDAO;
import com.cqi.hr.dao.CompanyLeaveDAO;
import com.cqi.hr.dao.GiveLeaveRecordDAO;
import com.cqi.hr.dao.GiveLeaveRuleDAO;
import com.cqi.hr.dao.SysUserDAO;
import com.cqi.hr.dao.UserAskForLeaveDAO;
import com.cqi.hr.dao.UserAskForOvertimeDAO;
import com.cqi.hr.dao.UserLeaveDAO;
import com.cqi.hr.dao.UserLeaveHistoryDAO;
import com.cqi.hr.dao.UserLeaveQuotaMonthlyDAO;
import com.cqi.hr.entity.CompanyLeave;
import com.cqi.hr.entity.GiveLeaveRecord;
import com.cqi.hr.entity.GiveLeaveRule;
import com.cqi.hr.entity.PagingList;
import com.cqi.hr.entity.SysUser;
import com.cqi.hr.entity.UserAskForLeave;
import com.cqi.hr.entity.UserLeave;
import com.cqi.hr.entity.UserLeaveHistory;
import com.cqi.hr.entity.UserLeaveQuotaMonthly;
import com.cqi.hr.util.DateUtils;
import com.cqi.hr.util.StringUtils;

@Transactional
@Service
public class UserLeaveService extends AbstractService<UserLeave> {
	@Resource
	UserLeaveDAO userLeaveDAO;
	@Resource
	UserLeaveHistoryDAO userLeaveHistoryDAO;
	@Resource
	UserLeaveQuotaMonthlyDAO userLeaveQuotaMonthlyDAO;
	@Resource
	CompanyLeaveDAO companyLeaveDAO;
	@Resource
	UserAskForLeaveDAO userAskForLeaveDAO;
	@Resource
	UserAskForOvertimeDAO userAskForOvertimeDAO;
	@Resource
	GiveLeaveRuleDAO giveLeaveRuleDAO;
	@Resource
	GiveLeaveRecordDAO giveLeaveRecordDAO;
	@Resource
	SysUserDAO sysUserDAO;

	@Resource
	UserAskForLeaveService userAskForLeaveService;

	@Override
	protected AbstractDAO<UserLeave> getDAO() {
		return userLeaveDAO;
	}

	@Transactional
	public List<UserLeave> getListByUserId(String userId) throws Exception {
		return userLeaveDAO.getListByUserId(userId);
	}

	@Transactional
	public PagingList<UserLeave> getListByPage(Integer page, String userId) throws Exception {
		return userLeaveDAO.getListByPage(page, userId);
	}

	@Transactional
	public List<CompanyLeave> getCompanyLeaveList() throws Exception {
		return companyLeaveDAO.getListByType(CompanyLeave.TYPE_LEAVE);
	}

	@Transactional
	public List<CompanyLeave> getCompanyLeaveListWithoutMenstruation() throws Exception {
		return companyLeaveDAO.getListWithoutMenstruation(CompanyLeave.TYPE_LEAVE);
	}

	@Transactional
	public List<CompanyLeave> getCompanyOvertimeList() throws Exception {
		return companyLeaveDAO.getListByType(CompanyLeave.TYPE_OVERTIME);
	}

	@Transactional
	public Map<Long, CompanyLeave> getCompanyLeaveMapping() throws Exception {
		Map<Long, CompanyLeave> mapping = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> list = companyLeaveDAO.getListByType(CompanyLeave.TYPE_LEAVE);
		for (CompanyLeave leave : list) {
			mapping.put(leave.getLeaveId(), leave);
		}
		return mapping;
	}

	@Transactional
	public Map<Long, CompanyLeave> getCompanyLeaveAllMapping() throws Exception {
		Map<Long, CompanyLeave> mapping = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> list = companyLeaveDAO.get();
		for (CompanyLeave leave : list) {
			mapping.put(leave.getLeaveId(), leave);
		}
		return mapping;
	}

	@Transactional
	public Map<Long, CompanyLeave> getCompanyOvertimeMapping() throws Exception {
		Map<Long, CompanyLeave> mapping = new HashMap<Long, CompanyLeave>();
		List<CompanyLeave> list = companyLeaveDAO.getListByType(CompanyLeave.TYPE_OVERTIME);
		for (CompanyLeave leave : list) {
			mapping.put(leave.getLeaveId(), leave);
		}
		return mapping;
	}

	@Transactional
	public PagingList<UserAskForLeave> getAskLeaveListByPage(Integer page, String userId) throws Exception {
		return userAskForLeaveDAO.getListByPage(page, userId);
	}

	@Transactional
	public List<UserAskForLeave> getAskForLeaveGroupByNow(String userId) throws Exception {
		return userAskForLeaveDAO.getSumListGroupByNow(userId);
	}

	/*
	 * 更新剩餘假期
	 * 
	 * @type 1是新增，2是更新
	 */
	@Transactional
	public boolean updateUserLeave(UserAskForLeave userAskForLeave, Integer type) throws Exception {
		CompanyLeave companyLeaveOri = companyLeaveDAO.get(userAskForLeave.getLeaveId());
		UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForLeave.getSysUserId(), userAskForLeave.getLeaveId());
		if (type.equals(1)) {
			if (null == userLeave) {
				userLeave = new UserLeave();
				userLeave.setLeaveId(userAskForLeave.getLeaveId());
				userLeave.setSysUserId(userAskForLeave.getSysUserId());
				userLeave.setCreateDate(new Date());
				userLeave.setUpdateDate(new Date());
				userLeave.setStatus(1);
				userLeave.setCount(0.0 + (companyLeaveOri.getType() * userAskForLeave.getSpendTime()));
				userLeaveDAO.persist(userLeave);
			} else {
				userLeave.setCount(userLeave.getCount() + (companyLeaveOri.getType() * userAskForLeave.getSpendTime()));
				userLeave.setUpdateDate(new Date());
				userLeaveDAO.update(userLeave);
			}
			userAskForLeaveDAO.persist(userAskForLeave);
		} else if (type.equals(2)) {
			UserAskForLeave userAskForLeaveOld = userAskForLeaveDAO.get(userAskForLeave.getAskForLeaveId());
			if (null == userAskForLeaveOld) {
				return false;
			} else {
				Calendar calendarLastMonth = Calendar.getInstance();
				calendarLastMonth.setTime(DateUtils.getFirstDateOfThisMonth());
				calendarLastMonth.add(Calendar.MONTH, -1);
				Calendar calendarLeaveOld = Calendar.getInstance();
				calendarLeaveOld.setTime(userAskForLeaveOld.getStartTime());
				if (calendarLastMonth.getTime().after(calendarLeaveOld.getTime())) {
					// 上個月的請假資料不得修改
					return false;
				}
				if (userAskForLeaveOld.getLeaveId().equals(userAskForLeave.getLeaveId())) {
//					logger.info("userLeave : " + BeanUtils.describe(userLeave).toString());
//					logger.info("cqiLeaveOri : " + BeanUtils.describe(companyLeaveOri).toString());
//					logger.info("userAskForLeaveOld : " + BeanUtils.describe(userAskForLeaveOld).toString());
//					logger.info("userAskForLeave : " + BeanUtils.describe(userAskForLeave).toString());
					userLeave.setCount(
							userLeave.getCount() - (companyLeaveOri.getType() * userAskForLeaveOld.getSpendTime())
									+ (companyLeaveOri.getType() * userAskForLeave.getSpendTime()));
					userLeave.setUpdateDate(new Date());
					userLeaveDAO.update(userLeave);
				} else {
					CompanyLeave cqiLeaveOld = companyLeaveDAO.get(userAskForLeaveOld.getLeaveId());
					UserLeave userLeaveOld = userLeaveDAO.getOneBy2Id(userAskForLeaveOld.getSysUserId(),
							userAskForLeaveOld.getLeaveId());
					if (null == userLeaveOld || null == cqiLeaveOld) {
						return false;
					} else {
						userLeaveOld.setCount(
								userLeaveOld.getCount() - (cqiLeaveOld.getType() * userAskForLeaveOld.getSpendTime()));
						userLeaveDAO.update(userLeaveOld);
						if (null == userLeave) {
							userLeave = new UserLeave();
							userLeave.setLeaveId(userAskForLeave.getLeaveId());
							userLeave.setSysUserId(userAskForLeave.getSysUserId());
							userLeave.setCreateDate(new Date());
							userLeave.setUpdateDate(new Date());
							userLeave.setStatus(1);
							userLeave.setCount(0.0 + (companyLeaveOri.getType() * userAskForLeave.getSpendTime()));
							userLeaveDAO.persist(userLeave);
						} else {
							userLeave.setCount(userLeave.getCount()
									+ (companyLeaveOri.getType() * userAskForLeave.getSpendTime()));
							userLeave.setUpdateDate(new Date());
							userLeaveDAO.update(userLeave);
						}
					}
				}
				userAskForLeave.setCreateDate(userAskForLeaveOld.getCreateDate());
				userAskForLeaveDAO.merge(userAskForLeave);
			}
		}
		return true;
	}

	/*
	 * 刪除請假紀錄
	 */
	@Transactional
	public boolean deleteAskLeave(Long askForLeaveId, SysUser operator) throws Exception {
		UserAskForLeave userAskForLeave = userAskForLeaveDAO.get(askForLeaveId);
		if (null == userAskForLeave) {
			return false;
		}
		if (!userAskForLeave.getSysUserId().equals(operator.getSysUserId())) {
			return false;
		}
		UserLeave userLeave = userLeaveDAO.getOneBy2Id(userAskForLeave.getSysUserId(), userAskForLeave.getLeaveId());
		if (null == userLeave) {
			return false;
		}
		CompanyLeave companyLeave = companyLeaveDAO.get(userAskForLeave.getLeaveId());
		if (null == companyLeave) {
			return false;
		}
		String errorMsg = userAskForLeaveService.checkRule(userAskForLeave);
		if (StringUtils.hasText(errorMsg)) {
			return false;
		}
		userLeave.setCount(userLeave.getCount() - (companyLeave.getType() * userAskForLeave.getSpendTime()));
		userLeaveDAO.update(userLeave);
		userAskForLeave.setStatus(0);
		userAskForLeave.setUpdateDate(new Date());
		userAskForLeaveDAO.update(userAskForLeave);
		return true;
	}

	/**
	 * Get Leave History By Year and Month
	 * 
	 * @throws Exception
	 */
	@Transactional
	public List<UserLeaveHistory> getLeaveHistoryByYearAndMonth(Integer year, Integer month, SysUser operator)
			throws Exception {
		return userLeaveHistoryDAO.getListByYearAndMonth(year, month, operator);
	}

	/*
	 * UserLeave新月份更新
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void monthlyLeaveDataUpdate() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONDAY, -1);
		Map<String, SysUser> userMapping = sysUserDAO.getMapEnableUserOrGraduationInMonth(calendar.getTime());
		List<UserLeave> userLeaveList = userLeaveDAO.get();
		// 取得判斷Type是-1的資料(月結會歸零)的Map
		Map<Long, CompanyLeave> mapLeaveType = (Map<Long, CompanyLeave>) companyLeaveDAO.queryToMap("leaveId");
		for (UserLeave userLeave : userLeaveList) {
			// 特休額度刷新，判斷到職日
			if (userMapping.get(userLeave.getSysUserId()) != null) {
				switch (mapLeaveType.get(userLeave.getLeaveId()).getLeaveName()) {
				case CompanyLeave.OCCUPIED_LEAVE:
					// 事假，搜尋下個月是否有事假與調班紀錄，有的話加減回來判斷是否負值，負值當月會扣薪，下個月初始化為零。
					Double afterAskForLeaveData = userAskForLeaveDAO.getAfterDataByMonth(userLeave.getSysUserId(),
							userLeave.getLeaveId());
					Double afterAskForOvertimeData = userAskForOvertimeDAO.getAfterDataByMonth(userLeave.getSysUserId(),
							CompanyLeave.SHIFT_OVERTIME_ID);
					Double monthSummary = userLeave.getCount() + afterAskForLeaveData - afterAskForOvertimeData;
					if (monthSummary < 0) {
						userLeave.setCount(afterAskForOvertimeData - afterAskForLeaveData);
						userLeaveDAO.update(userLeave);
					}
					// 記載在月結剩餘額度上，目前僅特休與事假有額度
					UserLeaveQuotaMonthly userLeaveQuotaMonthlyOccupied = new UserLeaveQuotaMonthly();
					userLeaveQuotaMonthlyOccupied.setMonthlySummaryQuota(monthSummary);
					userLeaveQuotaMonthlyOccupied.setDateOfYear(calendar.get(Calendar.YEAR));
					userLeaveQuotaMonthlyOccupied.setDateOfMonth(calendar.get(Calendar.MONTH) + 1); // 人看得懂的月份
					userLeaveQuotaMonthlyOccupied.setLeaveId(userLeave.getLeaveId());
					userLeaveQuotaMonthlyOccupied.setSysUserId(userLeave.getSysUserId());
					userLeaveQuotaMonthlyOccupied.setStatus(Constant.STATUS_ENABLE);
					userLeaveQuotaMonthlyOccupied.setCreateDate(new Date());
					UserLeaveQuotaMonthly oldOccupiedDataCheck = userLeaveQuotaMonthlyDAO.getOneByYearAndMonth(
							userLeaveQuotaMonthlyOccupied.getDateOfYear(),
							userLeaveQuotaMonthlyOccupied.getDateOfMonth(), userLeave.getLeaveId(),
							userLeaveQuotaMonthlyOccupied.getSysUserId());
					if (null == oldOccupiedDataCheck) {
						userLeaveQuotaMonthlyDAO.persist(userLeaveQuotaMonthlyOccupied);
					} else {
						oldOccupiedDataCheck
								.setMonthlySummaryQuota(userLeaveQuotaMonthlyOccupied.getMonthlySummaryQuota());
						oldOccupiedDataCheck.setUpdateDate(new Date());
						userLeaveQuotaMonthlyDAO.update(oldOccupiedDataCheck);
					}
					break;
				case CompanyLeave.SHIFT_OVERTIME:
					// 調班
					// Table : UserLeave沒有調班資料
					break;
				case CompanyLeave.MENSTRUATION_LEAVE:
					// 生理假，每個月額度一次，下個月初始化為零
					if (StringUtils.hasText(userMapping.get(userLeave.getSysUserId()).getGender())
							&& userMapping.get(userLeave.getSysUserId()).getGender().equals(Constant.GENDER_FEMALE)) {
						if (userMapping.get(userLeave.getSysUserId()).getStatus().equals(Constant.SYSUSER_ENABLE)) {
							userLeave.setCount(1.0);
						}
					} else {
						userLeave.setCount(0.0);
					}
					userLeaveDAO.update(userLeave);
					break;
				case CompanyLeave.ANNUAL_LEAVE:

					// 特休
					/**
					 * 勞基法特休 https://law.moj.gov.tw/LawClass/LawParaDeatil.aspx?pcode=N0030001&bp=4
					 * 一、六個月以上一年未滿者，三日。 二、一年以上二年未滿者，七日。 三、二年以上三年未滿者，十日。 四、三年以上五年未滿者，每年十四日。
					 * 五、五年以上十年未滿者，每年十五日。 六、十年以上者，每一年加給一日，加至三十日為止。
					 */
					Double afterAskForLeaveAnnualData = userAskForLeaveDAO.getAfterDataByMonth(userLeave.getSysUserId(),
							userLeave.getLeaveId());
					Double monthSummaryAnnual = userLeave.getCount() + afterAskForLeaveAnnualData;
					// 記載在月結剩餘額度上，目前僅特休與事假有額度
					UserLeaveQuotaMonthly userLeaveQuotaMonthlyAnnual = new UserLeaveQuotaMonthly();
					userLeaveQuotaMonthlyAnnual.setMonthlySummaryQuota(monthSummaryAnnual);
					userLeaveQuotaMonthlyAnnual.setDateOfYear(calendar.get(Calendar.YEAR));
					userLeaveQuotaMonthlyAnnual.setDateOfMonth(calendar.get(Calendar.MONTH) + 1); // 人看得懂的月份
					userLeaveQuotaMonthlyAnnual.setLeaveId(userLeave.getLeaveId());
					userLeaveQuotaMonthlyAnnual.setSysUserId(userLeave.getSysUserId());
					userLeaveQuotaMonthlyAnnual.setStatus(Constant.STATUS_ENABLE);
					userLeaveQuotaMonthlyAnnual.setCreateDate(new Date());
					UserLeaveQuotaMonthly oldAnnualDataCheck = userLeaveQuotaMonthlyDAO.getOneByYearAndMonth(
							userLeaveQuotaMonthlyAnnual.getDateOfYear(), userLeaveQuotaMonthlyAnnual.getDateOfMonth(),
							userLeave.getLeaveId(), userLeaveQuotaMonthlyAnnual.getSysUserId());
					if (null == oldAnnualDataCheck) {
						userLeaveQuotaMonthlyDAO.persist(userLeaveQuotaMonthlyAnnual);
					} else {
						oldAnnualDataCheck.setMonthlySummaryQuota(userLeaveQuotaMonthlyAnnual.getMonthlySummaryQuota());
						oldAnnualDataCheck.setUpdateDate(new Date());
						userLeaveQuotaMonthlyDAO.update(oldAnnualDataCheck);
					}
					break;
				case CompanyLeave.SICK_LEAVE:
					// 病假下個月初始化為零。取出新月份的請假設定
					Double afterAskForLeaveSickData = userAskForLeaveDAO.getAfterDataByMonth(userLeave.getSysUserId(),
							userLeave.getLeaveId());
					userLeave.setCount(-1 * afterAskForLeaveSickData);
					userLeaveDAO.update(userLeave);
					break;
				default:
					// 預設為，每月月初將請假時數歸零
					// 保留新的月份以後的紀錄
					Double afterDefaultData = userAskForLeaveDAO.getAfterDataByMonth(userLeave.getSysUserId(),
							userLeave.getLeaveId());
					userLeave.setCount(afterDefaultData);
					userLeaveDAO.update(userLeave);
					break;
				}
			}
		}
	}

	/**
	 * 產生特休假時數
	 * 
	 * @throws Exception
	 */
	@Transactional
	public void annualLeaveGive() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		// 找出特休規則
		List<GiveLeaveRule> giveLeaveRuleList = giveLeaveRuleDAO.getGiveLeaveRule(CompanyLeave.ANNUAL_LEAVE_ID);
		logger.info("giveLeaveRuleList : " + giveLeaveRuleList.size());

		// 根據規則搜尋符合的人，為確定有給假，符合條件加寬十天
		for (GiveLeaveRule giveLeaveRule : giveLeaveRuleList) {
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.setTime(DateUtils.getTodayWithoutHourMinSec());
			calendarStart.add(Calendar.DAY_OF_MONTH, -10);

			Calendar calendarEnd = Calendar.getInstance();
			calendarEnd.setTime(DateUtils.getTodayWithoutHourMinSec());

			logger.info("getYearsAfterAppointment : " + giveLeaveRule.getYearsAfterAppointment().toString());
			// 因為getYearsAfterAppointment為double，所以正整數會帶.0，因此調整StringUtils
			// isIntegerStartWithOne
			if (StringUtils.isIntegerStartWithOne(giveLeaveRule.getYearsAfterAppointment().toString())) {
				// 如果是整數年
				logger.info("整年");
				calendarStart.add(Calendar.YEAR, -1 * giveLeaveRule.getYearsAfterAppointment().intValue());
				calendarEnd.add(Calendar.YEAR, -1 * giveLeaveRule.getYearsAfterAppointment().intValue());
			} else {
				// 如果不是整數年
				logger.info("月份");
				Integer monthQuota = (int) (-1 * 12 * giveLeaveRule.getYearsAfterAppointment());
				calendarStart.add(Calendar.MONTH, monthQuota);
				calendarEnd.add(Calendar.MONTH, monthQuota);
			}
			logger.info("calendarStart : " + sdf.format(calendarStart.getTime()));
			logger.info("calendarEnd : " + sdf.format(calendarEnd.getTime()));
			List<SysUser> userList = sysUserDAO.getIntervalOfInauguration(calendarStart.getTime(),
					calendarEnd.getTime());
			logger.info("userList : " + userList.size());
			for (SysUser sysUser : userList) {
				// 確認是否已經給假，還沒給的依照規則給予假
				GiveLeaveRecord giveLeaveRecord = giveLeaveRecordDAO.getWithUserAndRule(sysUser.getSysUserId(),
						giveLeaveRule.getRuleId(), giveLeaveRule.getLeaveId());
				if (null == giveLeaveRecord) {
					logger.info("null==giveLeaveRecord");
					UserLeave userLeave = userLeaveDAO.getOneBy2Id(sysUser.getSysUserId(), giveLeaveRule.getLeaveId());
					if (null == userLeave) {
						logger.info("null==userLeave");
						userLeave = new UserLeave();
						userLeave.setLeaveId(giveLeaveRule.getLeaveId());
						userLeave.setSysUserId(sysUser.getSysUserId());
						// 特休目前以小時計算，一天工時八小時
						userLeave.setCount(giveLeaveRule.getQuota() * 8);
						userLeave.setStatus(Constant.STATUS_ENABLE);
						userLeave.setCreateDate(new Date());
						userLeaveDAO.persist(userLeave);
					} else {
						logger.info("null==userLeave else");
						// 特休目前以小時計算，一天工時八小時
						userLeave.setCount(userLeave.getCount() + (giveLeaveRule.getQuota() * 8));
						userLeaveDAO.update(userLeave);
					}
					giveLeaveRecord = new GiveLeaveRecord();
					giveLeaveRecord.setSysUserId(sysUser.getSysUserId());
					giveLeaveRecord.setRuleId(giveLeaveRule.getRuleId());
					giveLeaveRecord.setLeaveId(giveLeaveRule.getLeaveId());
					giveLeaveRecord.setQuota(giveLeaveRule.getQuota());
					giveLeaveRecord.setDescription("這邊紀錄的是天數，在UserLeave上的特休數是小時計算。所以會是quota乘以8");
					giveLeaveRecord.setStatus(Constant.STATUS_ENABLE);
					giveLeaveRecord.setCreateDate(new Date());
					giveLeaveRecordDAO.persist(giveLeaveRecord);
				}
				// 已經有給的就跳過
			}

		}
	}

	@Transactional
	public void menstruationLeaveGive() throws Exception {
		List<SysUser> femaleList = sysUserDAO.getFemale();
		for (SysUser female : femaleList) {
			UserLeave menstruationLeave = userLeaveDAO.getOneBy2Id(female.getSysUserId(),
					CompanyLeave.SHIFT_MENSTRUATION_ID);
			if (null == menstruationLeave) {
				menstruationLeave = new UserLeave();
				menstruationLeave.setCount(1.0);
				menstruationLeave.setLeaveId(CompanyLeave.SHIFT_MENSTRUATION_ID);
				menstruationLeave.setStatus(Constant.STATUS_ENABLE);
				menstruationLeave.setCreateDate(new Date());
				menstruationLeave.setSysUserId(female.getSysUserId());
				userLeaveDAO.persist(menstruationLeave);
				logger.info("新增" + female.getUserName() + "的生理假");
			}
		}
	}

	@Transactional
	public void occupiedLeaveGive() throws Exception {
		// 找出事假規則
		List<GiveLeaveRule> giveLeaveRuleList = giveLeaveRuleDAO.getGiveLeaveRule(CompanyLeave.OCCUPIED_LEAVE_ID );
		logger.info("●●●give occupied Leave List : " + giveLeaveRuleList.size());
		LocalDate localDate = LocalDate.now();
		List<SysUser> users = sysUserDAO.getEnableUser();
		for (SysUser user : users) {
			// 先撈出是否給過記錄
			GiveLeaveRecord giveLeaveRecord = giveLeaveRecordDAO.getWithUserAndRule(user.getSysUserId(),giveLeaveRuleList.get(0).getRuleId(), CompanyLeave.OCCUPIED_LEAVE_ID);
			if (null == giveLeaveRecord) {
				// 無記錄則給該員事假額度並記錄
				saveAndGiveSickLeave(giveLeaveRuleList, user,CompanyLeave.OCCUPIED_LEAVE_ID);
				saveSickLeaveRecord(giveLeaveRuleList, user,CompanyLeave.OCCUPIED_LEAVE_ID);
			} else {
				// 有記錄則判斷是否跨年, 跨年則給該員事假額度並記錄
				if (giveLeaveRecord.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).getYear() < localDate.getYear()) {
					saveAndGiveSickLeave(giveLeaveRuleList, user,CompanyLeave.OCCUPIED_LEAVE_ID);
					saveSickLeaveRecord(giveLeaveRuleList, user,CompanyLeave.OCCUPIED_LEAVE_ID);
				}
			}
		}
	}
	
	@Transactional
	public void sickLeaveGive() throws Exception {
		// 找出病假規則
		List<GiveLeaveRule> giveLeaveRuleList = giveLeaveRuleDAO.getGiveLeaveRule(CompanyLeave.SICK_LEAVE_ID);
		logger.info("●●●give sick Leave List : " + giveLeaveRuleList.size());
		LocalDate localDate = LocalDate.now();
		List<SysUser> users = sysUserDAO.getEnableUser();
		for (SysUser user : users) {
			// 先撈出是否給過記錄
			GiveLeaveRecord giveLeaveRecord = giveLeaveRecordDAO.getWithUserAndRule(user.getSysUserId(),giveLeaveRuleList.get(0).getRuleId(), CompanyLeave.SICK_LEAVE_ID);
			if (null == giveLeaveRecord) {
				// 無記錄則給該員病假額度並記錄
				saveAndGiveSickLeave(giveLeaveRuleList, user,CompanyLeave.SICK_LEAVE_ID);
				saveSickLeaveRecord(giveLeaveRuleList, user,CompanyLeave.SICK_LEAVE_ID);
			} else {
				// 有記錄則判斷是否跨年, 跨年則給該員病假額度並記錄
				if (giveLeaveRecord.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).getYear() < localDate.getYear()) {
					saveAndGiveSickLeave(giveLeaveRuleList, user,CompanyLeave.SICK_LEAVE_ID);
					saveSickLeaveRecord(giveLeaveRuleList, user,CompanyLeave.SICK_LEAVE_ID);
				}
			}
		}
	}
	
	@Transactional
	private void saveAndGiveSickLeave(List<GiveLeaveRule> giveLeaveRuleList, SysUser user,long leaveId) throws Exception {
		UserLeave userLeave = userLeaveDAO.getOneBy2Id(user.getSysUserId(), leaveId);
		if (userLeave==null) {
			userLeave = new UserLeave();
			userLeave.setCount(giveLeaveRuleList.get(0).getQuota());
			userLeave.setLeaveId(leaveId);
			userLeave.setStatus(Constant.STATUS_ENABLE);
			userLeave.setCreateDate(new Date());
			userLeave.setUpdateDate(new Date());
			userLeave.setSysUserId(user.getSysUserId());
			userLeaveDAO.persist(userLeave);
			logByLeaveId(user, leaveId);
		}else {
			userLeave.setCount(giveLeaveRuleList.get(0).getQuota());
			userLeave.setUpdateDate(new Date());
			userLeaveDAO.update(userLeave);
			logByLeaveId(user, leaveId);
		}
	}

	private void logByLeaveId(SysUser user, long leaveId) {
		if (leaveId==1) {
			logger.info("●●●新增" + user.getUserName() + "的病假額度●●●");
		}else if (leaveId==5) {
			logger.info("●●●新增" + user.getUserName() + "的事假額度●●●");
		}
	}
	
	private void logRecordByLeaveId(SysUser user, long leaveId) {
		if (leaveId==1) {
			logger.info("●●●新增" + user.getUserName() + "的病假記錄●●●");
		}else if (leaveId==5) {
			logger.info("●●●新增" + user.getUserName() + "的事假記錄●●●");
		}
	}
	
	@Transactional
	private void saveSickLeaveRecord(List<GiveLeaveRule> giveLeaveRuleList, SysUser user ,long leaveId) throws Exception {
		GiveLeaveRecord giveLeaveRecord;
		giveLeaveRecord = new GiveLeaveRecord();
		giveLeaveRecord.setSysUserId(user.getSysUserId());
		giveLeaveRecord.setRuleId(giveLeaveRuleList.get(0).getRuleId());
		giveLeaveRecord.setLeaveId(giveLeaveRuleList.get(0).getLeaveId());
		giveLeaveRecord.setQuota(giveLeaveRuleList.get(0).getQuota());
		giveLeaveRecord.setDescription("在UserLeave上的休假數以小時計算。");
		giveLeaveRecord.setStatus(Constant.STATUS_ENABLE);
		giveLeaveRecord.setCreateDate(new Date());
		giveLeaveRecordDAO.persist(giveLeaveRecord);
		logRecordByLeaveId(user, leaveId);
	}
}
