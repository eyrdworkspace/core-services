package org.egov.demand.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.entity.EgBillDetails;
import org.egov.demand.persistence.repository.BillRepository;
import org.egov.demand.persistence.repository.BillTypeRepository;
import org.egov.demand.web.contract.BillDetailInfo;
import org.egov.demand.web.contract.BillInfo;
import org.egov.demand.web.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {
	private static final Logger LOGGER = Logger.getLogger(BillService.class);
	@Autowired
	private BillRepository billRepository;
	@Autowired
	private ModuleRepository moduleRepository;
	@Autowired
	private BillTypeRepository billTypeRepository;

	public EgBill createBill(Long demandId, BillInfo billInfo) {
		EgBill egBill = new EgBill(billInfo);
		List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
		egBill.setEgDemand(demandId);
		egBill.setModule(moduleRepository.fetchModuleByName(billInfo.getModuleName()).getId());
		egBill.setEgBillType(billTypeRepository.findByName(billInfo.getBillType()));
		egBill.setUserId(1l);
		for (BillDetailInfo billDetailInfo : billInfo.getBillDetailInfos()) {
			EgBillDetails egBillDetail = new EgBillDetails(billDetailInfo);
			egBillDetail.setEgBill(egBill);
			billDetails.add(egBillDetail);
		}
		egBill.setEgBillDetails(billDetails);
		return billRepository.save(egBill);
	}
}