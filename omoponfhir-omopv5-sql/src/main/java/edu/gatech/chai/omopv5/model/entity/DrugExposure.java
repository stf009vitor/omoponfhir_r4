/*******************************************************************************
 * Copyright (c) 2019 Georgia Tech Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package edu.gatech.chai.omopv5.model.entity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import edu.gatech.chai.omopv5.model.entity.custom.Column;
import edu.gatech.chai.omopv5.model.entity.custom.GeneratedValue;
import edu.gatech.chai.omopv5.model.entity.custom.GenerationType;
import edu.gatech.chai.omopv5.model.entity.custom.Id;
import edu.gatech.chai.omopv5.model.entity.custom.JoinColumn;
import edu.gatech.chai.omopv5.model.entity.custom.Table;

@Table(name = "fhir_drug_exposure")
public class DrugExposure extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="drug_exposure_id_seq")
	@Column(name="drug_exposure_id", nullable=false)
	private Long id;
	
	@JoinColumn(name="person_id", table="f_person:fPerson,person:person", nullable=false)
	private FPerson fPerson;
	
	@JoinColumn(name="drug_concept_id", referencedColumnName="concept_id", nullable=false)
	private Concept drugConcept;
	
//-------NEW------------------------------------------------------------------------------------------------------------------------------
	@Column(name="drug_name", nullable=false)
	private String drug_name;	

	@Column(name="drug_RxNorm_code", nullable=false)
	private String drug_RxNorm_code;	

	@Column(name="drug_NDC_code", nullable=false)
	private String drug_NDC_code;	

	@Column(name="drug_other_code", nullable=false)
	private String drug_other_code;	

	@Column(name="drug_other_code_system", nullable=false)
	private String drug_other_code_system;	
	
	@Column(name="drug_indication", nullable=false)
	private String drug_indication;		

	@Column(name="drug_min_dose_value", nullable=false)
	private long drug_min_dose_value;	

	@Column(name="drug_min_dose_unit", nullable=false)
	private String drug_min_dose_unit;	

	@Column(name="drug_max_dose_value", nullable=false)
	private long drug_max_dose_value;	

	@Column(name="drug_max_dose_unit", nullable=false)
	private String drug_max_dose_unit;	

	@Column(name="drug_frequency", nullable=false)
	private String drug_frequency;	

	@Column(name="drug_quantity_ordered_value", nullable=false)
	private long drug_quantity_ordered_value;	

	@Column(name="drug_quantity_ordered_unit", nullable=false)
	private String drug_quantity_ordered_unit;		
	
	@Column(name="drug_route", nullable=false)
	private String drug_route;	

	@Column(name="drug_order_status", nullable=false)
	private String drug_order_status;	

	@Column(name="drug_quantity_dispensed_value", nullable=false)
	private long drug_quantity_dispensed_value;	

	@Column(name="drug_quantity_dispensed_unit", nullable=false)
	private String drug_quantity_dispensed_unit;	

	@Column(name="rate", nullable=false)
	private String rate;		
//----------------------------------------------------------------------------------------------------------------------------------------	
	
	@Column(name="drug_exposure_start_date", nullable=false)
	private Date drugExposureStartDate;
	
	@Column(name="drug_exposure_start_datetime")
	private Date drugExposureStartDateTime;

	@Column(name="drug_exposure_end_date", nullable=false)
	private Date drugExposureEndDate;
	
	@Column(name="drug_exposure_end_datetime")
	private Date drugExposureEndDateTime;

	@Column(name="verbatim_end_date")
	private Date verbatimEndDate;

	@JoinColumn(name="drug_type_concept_id", referencedColumnName="concept_id", nullable=false)
	private Concept drugTypeConcept;
	
	@Column(name="stop_reason")
	private String stopReason;
	
	@Column(name="refills")
	private Integer refills;
	
	@Column(name="quantity")
	private Double quantity;

	@Column(name="days_supply")
	private Integer daysSupply;
	
	@Column(name="sig")
	private String sig;
	
	@JoinColumn(name="route_concept_id", referencedColumnName="concept_id")
	private Concept routeConcept;
	
	@Column(name="lot_number")
	private String lotNumber;
	
	@JoinColumn(name="provider_id")
	private Provider provider;
	
	@JoinColumn(name="visit_occurrence_id")
	private VisitOccurrence visitOccurrence;
	
	@Column(name="drug_source_value")
	private String drugSourceValue;
	
	@JoinColumn(name="drug_source_concept_id", referencedColumnName="concept_id")
	private Concept drugSourceConcept;
	
	@Column(name="route_source_value")
	private String routeSourceValue;
	
	@Column(name="dose_unit_source_value")
	private String doseUnitSourceValue;
	
	public DrugExposure() {
	}

	public DrugExposure(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FPerson getFPerson() {
		return this.fPerson;
	}

	public void setFPerson(FPerson fPerson) {
		this.fPerson = fPerson;
	}
	
//-------NEW------------------------------------------------------------------------------------------------------------------------------
	//Drug Code
	public void set_drug_name(String drug_name) {
		this.drug_name = drug_name;
	}
	public String get_drug_name() {
		return drug_name;
	}
	public void set_drug_RxNorm_code(String drug_RxNorm_code) {
		this.drug_RxNorm_code = drug_RxNorm_code;
	}
	public String get_drug_RxNorm_code() {
		return drug_RxNorm_code;
	}
	public void set_drug_NDC_code(String drug_NDC_code) {
		this.drug_NDC_code = drug_NDC_code;
	}
	public String get_drug_NDC_code() {
		return drug_NDC_code;
	}
	public void set_drug_other_code(String drug_other_code) {
		this.drug_other_code = drug_other_code;
	}
	public String get_drug_other_code() {
		return drug_other_code;
	}	
	public void set_drug_other_code_system(String drug_other_code_system) {
		this.drug_other_code_system = drug_other_code_system;
	}
	public String get_drug_other_code_system() {
		return drug_other_code_system;
	}
	
	// Drug Signature
	public void set_drug_indication(String drug_indication) {
		this.drug_indication = drug_indication;
	}
	public String get_drug_indication() {
		return drug_indication;
	}
	
	//Drug Min Dose
	public void set_drug_min_dose_value(Double drug_min_dose_value) {
		this.drug_min_dose_value = drug_min_dose_value;
	}
	public Double get_drug_min_dose_value() {
		return drug_min_dose_value;
	}
	public void set_drug_min_dose_unit(String drug_min_dose_unit) {
		this.drug_min_dose_unit = drug_min_dose_unit;
	}
	public String get_drug_min_dose_unit() {
		return drug_min_dose_unit;
	}
	
	//Drug Max Dose
	public void set_drug_max_dose_value(Double drug_max_dose_value) {
		this.drug_max_dose_value = drug_max_dose_value;
	}
	public Double get_drug_max_dose_value() {
		return drug_max_dose_value;
	}
	public void set_drug_max_dose_unit(String drug_max_dose_unit) {
		this.drug_max_dose_unit = drug_max_dose_unit;
	}
	public String get_drug_max_dose_unit() {
		return drug_max_dose_unit;
	}
	
	//Drug Frequency
	public void set_drug_frequency(String drug_frequency) {
		this.drug_frequency = drug_frequency;
	}
	public String get_drug_frequency() {
		return drug_frequency;
	}
	
	//Drug Route
	public void set_drug_route(String drug_route) {
		this.drug_route = drug_route;
	}
	public String get_drug_route() {
		return drug_route;
	}
	
	//Drug Quantity Ordered
	public void set_drug_quantity_ordered_value(long drug_quantity_ordered_value) {
		this.drug_quantity_ordered_value = drug_quantity_ordered_value;
	}
	public long get_drug_quantity_ordered_value() {
		return drug_quantity_ordered_value;
	}
	public void set_drug_quantity_ordered_unit(String drug_quantity_ordered_unit) {
		this.drug_quantity_ordered_unit = drug_quantity_ordered_unit;
	}
	public String get_drug_quantity_ordered_unit() {
		return drug_quantity_ordered_unit;
	}
	
	//Drug Order Status
	public void set_drug_order_status(String drug_order_status) {
		this.drug_order_status = drug_order_status;
	}
	public String get_drug_order_status() {
		return drug_order_status;
	}
	
	//Quantity Dispensed
	public void set_drug_quantity_dispensed_value(long drug_quantity_dispensed_value) {
		this.drug_quantity_dispensed_value = drug_quantity_dispensed_value;
	}
	public long get_drug_quantity_dispensed_value() {
		return drug_quantity_dispensed_value;
	}
	public void set_drug_quantity_dispensed_unit(String drug_quantity_dispensed_unit) {
		this.drug_quantity_dispensed_unit = drug_quantity_dispensed_unit;
	}
	public String get_drug_quantity_dispensed_unit() {
		return drug_quantity_dispensed_unit;
	}
	
	//Rate
	public void set_rate(String rate) {
		this.rate = rate;
	}
	public String get_rate() {
		return rate;
	}
//----------------------------------------------------------------------------------------------------------------------------------------	

	public Concept getDrugConcept() {
		return this.drugConcept;
	}

	public void setDrugConcept(Concept drugConcept) {
		this.drugConcept = drugConcept;
	}

	public Date getDrugExposureStartDate() {
		return this.drugExposureStartDate;
	}

	public void setDrugExposureStartDate(Date drugExposureStartDate) {
		this.drugExposureStartDate = drugExposureStartDate;
	}

	public Date getDrugExposureStartDateTime() {
		return this.drugExposureStartDateTime;
	}

	public void setDrugExposureStartDateTime(Date drugExposureStartDateTime) {
		this.drugExposureStartDateTime = drugExposureStartDateTime;
	}

	public Date getDrugExposureEndDate() {
		return this.drugExposureEndDate;
	}

	public void setDrugExposureEndDate(Date drugExposureEndDate) {
		this.drugExposureEndDate = drugExposureEndDate;
	}

	public Date getDrugExposureEndDateTime() {
		return this.drugExposureEndDateTime;
	}

	public void setDrugExposureEndDateTime(Date drugExposureEndDateTime) {
		this.drugExposureEndDateTime = drugExposureEndDateTime;
	}

	public Date getVerbatimEndDate() {
		return this.verbatimEndDate;
	}

	public void setVerbatimEndDate(Date verbatimEndDate) {
		this.verbatimEndDate = verbatimEndDate;
	}

	public Concept getDrugTypeConcept() {
		return this.drugTypeConcept;
	}

	public void setDrugTypeConcept(Concept drugTypeConcept) {
		this.drugTypeConcept = drugTypeConcept;
	}

	public String getStopReason() {
		return this.stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	public Integer getRefills() {
		return this.refills;
	}

	public void setRefills(Integer refills) {
		this.refills = refills;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Integer getDaysSupply() {
		return this.daysSupply;
	}

	public void setDaysSupply(Integer daysSupply) {
		this.daysSupply = daysSupply;
	}

	public String getSig() {
		return this.sig;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	public Concept getRouteConcept() {
		return this.routeConcept;
	}

	public void setRouteConcept(Concept routeConcept) {
		this.routeConcept = routeConcept;
	}

	public String getLotNumber() {
		return this.lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public VisitOccurrence getVisitOccurrence() {
		return this.visitOccurrence;
	}

	public void setVisitOccurrence(VisitOccurrence visitOccurrence) {
		this.visitOccurrence = visitOccurrence;
	}

	public String getDrugSourceValue() {
		return drugSourceValue;
	}

	public void setDrugSourceValue(String drugSourceValue) {
		this.drugSourceValue = drugSourceValue;
	}

	public Concept getDrugSourceConcept() {
		return this.drugSourceConcept;
	}

	public void setDrugSourceConcpet(Concept drugSourceConcept) {
		this.drugSourceConcept = drugSourceConcept;
	}

	public String getRouteSourceValue() {
		return this.routeSourceValue;
	}

	public void setRouteSourceValue(String routeSourceValue) {
		this.routeSourceValue = routeSourceValue;
	}

	public String getDoseUnitSourceValue() {
		return doseUnitSourceValue;
	}

	public void setDoseUnitSourceValue(String doseUnitSourceValue) {
		this.doseUnitSourceValue = doseUnitSourceValue;
	}

	@Override
	public Long getIdAsLong() {
		return getId();
	}

	@Override
	public String getColumnName(String columnVariable) {
		return DrugExposure._getColumnName(columnVariable);
	}

	public static String _getColumnName(String columnVariable) {
		try {
			Field field = DrugExposure.class.getDeclaredField(columnVariable);
			if (field != null) {
				Column annotation = field.getDeclaredAnnotation(Column.class);
				if (annotation != null) {
					return DrugExposure._getTableName() + "." + annotation.name();
				} else {
					JoinColumn joinAnnotation = field.getDeclaredAnnotation(JoinColumn.class);
					if (joinAnnotation != null) {
						return DrugExposure._getTableName() + "." + joinAnnotation.name();
					}

					System.out.println("ERROR: annotation is null for field=" + field.toString());
					return null;
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return null;

//		if ("id".equals(columnVariable))
//			return "drug_exposure.drug_exposure_id";
//
//		if ("daysSupply".equals(columnVariable))
//			return "drug_exposure.days_supply";
//
//		if ("sig".equals(columnVariable))
//			return "drug_exposure.sig";
//
//		if ("routeConcept".equals(columnVariable))
//			return "drug_exposure.route_concept_id";
//
//		if ("effectiveDrugDose".equals(columnVariable))
//			return "drug_exposure.effective_drug_dose";
//
//		if ("doseUnitConcept".equals(columnVariable))
//			return "drug_exposure.dose_unit_concept_id";
//
//		if ("lotNumber".equals(columnVariable))
//			return "drug_exposure.lot_number";
//
//		if ("provider".equals(columnVariable))
//			return "drug_exposure.provider_id";
//
//		if ("visitOccurrence".equals(columnVariable))
//			return "drug_exposure.visit_occurrence_id";
//
//		if ("drugSourceValue".equals(columnVariable))
//			return "drug_exposure.drug_source_value";
//
//		if ("drugSourceConcept".equals(columnVariable))
//			return "drug_exposure.drug_source_concept_id";
//
//		if ("fPerson".equals(columnVariable))
//			return "drug_exposure.person_id";
//
//		if ("routeSourceValue".equals(columnVariable))
//			return "drug_exposure.route_source_value";
//
//		if ("doseUnitSourceValue".equals(columnVariable))
//			return "drug_exposure.dose_unit_source_value";
//
//		if ("drugConcept".equals(columnVariable))
//			return "drug_exposure.drug_concept_id";
//
//		if ("drugExposureStartDate".equals(columnVariable))
//			return "drug_exposure.drug_exposure_start_date";
//
//		if ("drugExposureEndDate".equals(columnVariable))
//			return "drug_exposure.drug_exposure_end_date";
//
//		if ("drugTypeConcept".equals(columnVariable))
//			return "drug_exposure.drug_type_concept_id";
//
//		if ("stopReason".equals(columnVariable))
//			return "drug_exposure.stop_reason";
//
//		if ("refills".equals(columnVariable))
//			return "drug_exposure.refills";
//
//		if ("quantity".equals(columnVariable))
//			return "drug_exposure.quantity";
//
//		return null;
	}

	@Override
	public String getTableName() {
		return DrugExposure._getTableName();
	}

	public static String _getTableName() {
		Table annotation = DrugExposure.class.getDeclaredAnnotation(Table.class);
		if (annotation != null) {
			return annotation.name();
		}
		return "drug_exposure";
	}

	@Override
	public String getForeignTableName(String foreignVariable) {
		return DrugExposure._getForeignTableName(foreignVariable);
	}
	
	public static String _getForeignTableName(String foreignVariable) {
		if ("routeConcept".equals(foreignVariable)
				|| "drugSourceConcept".equals(foreignVariable) 
				|| "drugTypeConcept".equals(foreignVariable)
				|| "drugConcept".equals(foreignVariable))
			return Concept._getTableName();

		if ("fPerson".equals(foreignVariable))
			return FPerson._getTableName();

		if ("provider".equals(foreignVariable))
			return Provider._getTableName();

		if ("visitOccurrence".equals(foreignVariable))
			return VisitOccurrence._getTableName();

		return null;
	}

	@Override
	public String getSqlSelectTableStatement(List<String> parameterList, List<String> valueList) {
		return DrugExposure._getSqlTableStatement(parameterList, valueList);
	}

	public static String _getSqlTableStatement(List<String> parameterList, List<String> valueList) {
		return "select * from drug_exposure ";
	}

}
