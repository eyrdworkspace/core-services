package org.egov.dataupload.property;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.egov.dataupload.property.models.Document;
import org.egov.dataupload.property.models.OwnerInfo;
import org.egov.dataupload.property.models.OwnerInfo.RelationshipEnum;
import org.egov.dataupload.property.models.Property;
import org.egov.dataupload.property.models.PropertyDetail;
import org.egov.dataupload.property.models.Unit;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyFileReader {

	public Map<String, Sheet> readFile(String location) throws InvalidFormatException, IOException {
		Map<String, Sheet> sheetMap = new HashMap<>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(location));

		// Retrieving the number of sheets in the Workbook
		log.info("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		workbook.forEach(sheet -> {
			log.info("=> " + sheet.getSheetName());
			sheetMap.put(sheet.getSheetName(), sheet);
		});
		workbook.close();

		return sheetMap;
	}

	public Map<String, Property> parseExcel(String location) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Map<String, Sheet> sheetMap = readFile(location);
		Map<String, Property> propertyIdMap = parsePropertyExcel(sheetMap);
		parseUnitDetail(sheetMap, propertyIdMap);
		parseOwnerDetail(sheetMap, propertyIdMap);
		
		return propertyIdMap;
		
	}
	public Map<String, Property>  parsePropertyExcel(Map<String, Sheet> sheetMap) {

		Sheet propertySheet = sheetMap.get("Property_Detail");

		Map<String, Property> propertyIdMap = new LinkedHashMap<>();
		Iterator<Row> rowIterator = propertySheet.rowIterator();
		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Property property = new Property();
			property.getPropertyDetails().get(0).setAdditionalDetails(new HashMap<String, Object>());
			Row row = rowIterator.next();
			
			if (rowNumber++ == 0)
				continue;
			if(StringUtils.isEmpty(row.getCell(0))){
				break;
			}
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setPropertyDetails(cell, property);
			}
			propertyIdMap.put(property.getOldPropertyId(), property);
		}
		
		return propertyIdMap;
	}

	@SuppressWarnings("unchecked")
	private void setPropertyDetails(Cell cell, Property property) {
		switch (cell.getColumnIndex()) {
		case 0:
			property.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			cell.setCellType(CellType.STRING);
			property.getAddress().setCity(cell.getStringCellValue());
			break;
		case 2:
			property.setOldPropertyId(cell.getStringCellValue());
			break;
		case 3:
			property.getPropertyDetails().get(0).setFinancialYear(cell.getStringCellValue());
			break;
		case 4:
			property.getPropertyDetails().get(0).setPropertyType(cell.getStringCellValue());
			break;
		case 5:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getPropertyDetails().get(0).setPropertySubType(cell.getStringCellValue());
			break;
		case 6:
			Map<String, Object> addDetails = (Map<String, Object>) property.getPropertyDetails().get(0)
					.getAdditionalDetails();
			addDetails.put("heightAbove36Feet", cell.getBooleanCellValue());
			break;
		case 7:
			Map<String, Object> addDetailsMap = (Map<String, Object>) property.getPropertyDetails().get(0)
					.getAdditionalDetails();
			addDetailsMap.put("inflammable", cell.getBooleanCellValue());
			break;
		case 8:
			property.getPropertyDetails().get(0).setUsageCategoryMajor(cell.getStringCellValue());
			break;
		case 9:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getPropertyDetails().get(0).setUsageCategoryMinor(cell.getStringCellValue());
			break;
			// in case of shared property value should go to built up area else land area
		case 10:
			if ("SHAREDPROPERTY".equalsIgnoreCase(property.getPropertyDetails().get(0).getPropertySubType()))
				property.getPropertyDetails().get(0).setBuildUpArea((float) cell.getNumericCellValue());
			else
				property.getPropertyDetails().get(0).setLandArea((float) cell.getNumericCellValue());
			break;
		case 11:
			property.getPropertyDetails().get(0).setNoOfFloors((long) cell.getNumericCellValue());
			break;
		case 12:
			property.getPropertyDetails().get(0).setOwnershipCategory(cell.getStringCellValue());
			break;
		case 13:
			property.getPropertyDetails().get(0).setSubOwnershipCategory(cell.getStringCellValue());
			break;
		case 14:
			property.getAddress().getLocality().setCode(cell.getStringCellValue());
			break;
		case 15:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setDoorNo(cell.getStringCellValue());
			break;
		case 16:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setBuildingName(cell.getStringCellValue());
			break;
		case 17:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setStreet(cell.getStringCellValue());
			break;
		case 18:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setPincode(cell.getStringCellValue());
			break;

		default:
			break;
		}

		System.out.print("\t");
	}

	private void parseUnitDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Unit_Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			if(StringUtils.isEmpty(row.getCell(0))){
				break;
			}

			String propertyId = row.getCell(1).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);

			if (null == property)
				continue;
			
			Unit unit = new Unit();

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setUnitDetails(cell, unit);
			}
			property.getPropertyDetails().get(0).getUnits().add(unit);
		}
	}

	private void setUnitDetails(Cell cell, Unit unit) {

		switch (cell.getColumnIndex()) {
		case 0:
			unit.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			break;
		case 2:
			unit.setFloorNo(String.valueOf((int)cell.getNumericCellValue()));
			break;
		case 3: 
			break;
		case 4:
			unit.setUsageCategoryMajor(cell.getStringCellValue());
			break;
		case 5:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategoryMinor(cell.getStringCellValue());
			break;
		case 6:
			unit.setOccupancyType(cell.getStringCellValue());
			break;
		case 7:
			unit.setUnitArea((float) cell.getNumericCellValue());
			break;
		case 8:
			unit.setArv(new BigDecimal(cell.getNumericCellValue(), MathContext.DECIMAL64));
			break;
		case 9:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategorySubMinor(cell.getStringCellValue());
			break;
		case 10:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategoryDetail(cell.getStringCellValue());
			break;
		default:
			break;
		}

		System.out.print("\t");
	}

	private void parseOwnerDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Owner_Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			if(StringUtils.isEmpty(row.getCell(0))){
				break;
			}

			String propertyId = row.getCell(1).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);
			if (null == property)
				continue;
			PropertyDetail dtl = property.getPropertyDetails().get(0);
			OwnerInfo owner = new OwnerInfo();
			Document ownerDoc = new Document();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setOwnerDetails(cell, owner, ownerDoc);
			}
			
			// setting documents to owner
			HashSet<Document> docs = new HashSet<>();
			docs.add(ownerDoc);
			owner.setDocuments(docs);
			
			/*
			 * Adding citizen info. 
			 * The first owner object encountered for a property will be
			 * set as primary owner
			 */
			if (CollectionUtils.isEmpty(dtl.getOwners())) dtl.setCitizenInfo(owner);
			dtl.getOwners().add(owner);
		}
	}

	private void setOwnerDetails(Cell cell, OwnerInfo ownerInfo, Document document) {

		switch (cell.getColumnIndex()) {
		case 0:
			ownerInfo.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			break;
		case 2:
			ownerInfo.setName(cell.getStringCellValue());
			break;
		case 3:
			cell.setCellType(CellType.STRING);
			ownerInfo.setMobileNumber(cell.getStringCellValue());
			break;
		case 4:
			ownerInfo.setFatherOrHusbandName(cell.getStringCellValue());
			break;
		case 5:
			ownerInfo.setRelationship(RelationshipEnum.fromValue(cell.getStringCellValue()));
			break;
		case 6:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setPermanentAddress(cell.getStringCellValue());
			break;

		case 7:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setOwnerType(cell.getStringCellValue());
			break;
		case 8:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				document.setDocumentType(cell.getStringCellValue());
			break;
		case 9:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				document.setDocumentUid(cell.getStringCellValue());
			break;
		case 10:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setEmailId(cell.getStringCellValue());
			break;
		case 11:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setGender(cell.getStringCellValue());
			break;
		
		default:
			break;
		}

		System.out.print("\t");
	}
}
