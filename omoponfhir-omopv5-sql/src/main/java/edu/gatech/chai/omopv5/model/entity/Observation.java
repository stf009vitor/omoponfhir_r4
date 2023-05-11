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

@Table(name = "observation")
public class Observation extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="observation_id_seq")
	@Column(name="observation_id", nullable=false)
	private Long id;
	
	@JoinColumn(name="person_id", table="f_person:fPerson,person:person", nullable=false)
	private FPerson fPerson;
	
	@JoinColumn(name="observation_concept_id", referencedColumnName="concept_id", nullable=false)
	private Concept observationConcept;
	
	@Column(name="observation_date", nullable=false)
	private Date observationDate;
	
	@Column(name="observation_datetime")
	private Date observationDateTime;
	
	@JoinColumn(name="observation_type_concept_id", referencedColumnName="concept_id", nullable=false)
	private Concept observationTypeConcept;
	
	@Column(name="value_as_number")
	private Double valueAsNumber;

//-------NEW------------------------------------------------------------------------------------------------------------------------------
	@Column(name="lab_txt", nullable=false)
	private String lab_txt;	

	@Column(name="lab_cd", nullable=false)
	private String lab_cd;	

	@Column(name="lab_cdsys", nullable=false)
	private String lab_cdsys;	

	@Column(name="lab_rslt_lln_num", nullable=false)
	private String lab_rslt_lln_num;	

	@Column(name="lab_rslt_lln_unit", nullable=false)
	private String lab_rslt_lln_unit;	

	@Column(name="lab_rslt_uln_num", nullable=false)
	private String lab_rslt_uln_num;	

	@Column(name="lab_rslt_uln_unit", nullable=false)
	private String lab_rslt_uln_unit;	

	@Column(name="img_mod_txt", nullable=false)
	private String img_mod_txt;	

	@Column(name="img_prc_txt", nullable=false)
	private String img_prc_txt;	

	@Column(name="img_rsn_txt", nullable=false)
	private String img_rsn_txt;	

	@Column(name="img_dscrp_txt", nullable=false)
	private String img_dscrp_txt;	

	@Column(name="img_status_txt", nullable=false)
	private String img_status_txt;		
//----------------------------------------------------------------------------------------------------------------------------------------
	@Column(name="value_as_string")
	private String valueAsString;
	
	@JoinColumn(name="value_as_concept_id", referencedColumnName="concept_id")
	private Concept valueAsConcept;
	
	@JoinColumn(name="qualifier_concept_id", referencedColumnName="concept_id")
	private Concept qualifierConcept;
	
	@JoinColumn(name="unit_concept_id", referencedColumnName="concept_id")
	private Concept unitConcept;
	
	@JoinColumn(name="provider_id")
	private Provider provider;
	
	@JoinColumn(name="visit_occurrence_id")
	private VisitOccurrence visitOccurrence;
	
	@Column(name="observation_source_value")
	private String observationSourceValue;
	
	@JoinColumn(name="observation_source_concept_id", referencedColumnName="concept_id")
	private Concept observationSourceConcept;
	
	@Column(name="unit_source_value")
	private String unitSourceValue;

	@Column(name="qualifier_source_value")
	private String qualifierSourceValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FPerson getFPerson() {
		return fPerson;
	}

	public void setFPerson(FPerson fPerson) {
		this.fPerson = fPerson;
	}

	public Concept getObservationConcept() {
		return observationConcept;
	}

	public void setObservationConcept(Concept observationConcept) {
		this.observationConcept = observationConcept;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

//-------NEW------------------------------------------------------------------------------------------------------------------------------
	// Lab Exam Text
	public void set_lab_txt(String lab_txt) {
		this.lab_txt = lab_txt;
	}
	public String get_lab_txt() {
		return lab_txt;
	}
	
	//Lab Exam Code
	public void set_lab_cd(String lab_cd) {
		this.lab_cd = lab_cd;
	}
	public String get_lab_cd() {
		return lab_cd;
	}
	
	//Lab Exam System
	public void set_lab_cdsys(String lab_cdsys) {
		this.lab_cdsys = lab_cdsys;
	}
	public String get_lab_cdsys() {
		return lab_cdsys;
	}
	
	//Lab Results Range Values
	public void set_lab_rslt_lln_num(String lab_rslt_lln_num) {
		this.lab_rslt_lln_num = lab_rslt_lln_num;
	}
	public String get_lab_rslt_lln_num() {
		return lab_rslt_lln_num;
	}
	public void set_lab_rslt_lln_unit(String lab_rslt_lln_unit) {
		this.lab_rslt_lln_unit = lab_rslt_lln_unit;
	}
	public String get_lab_rslt_lln_unit() {
		return lab_rslt_lln_unit;
	}
	public void set_lab_rslt_uln_num(String lab_rslt_uln_num) {
		this.lab_rslt_uln_num = lab_rslt_uln_num;
	}
	public String get_lab_rslt_uln_num() {
		return lab_rslt_uln_num;
	}
	public void set_lab_rslt_uln_unit(String lab_rslt_uln_unit) {
		this.lab_rslt_uln_unit = lab_rslt_uln_unit;
	}
	public String get_lab_rslt_uln_unit() {
		return lab_rslt_uln_unit;
	}
	
	//Image Exam Modality
	public void set_img_mod_txt(String img_mod_txt) {
		this.img_mod_txt = img_mod_txt;
	}
	public String get_img_mod_txt() {
		return img_mod_txt;
	}
	
	//Image Exam Procedure
	public void set_img_prc_txt(String img_prc_txt) {
		this.img_prc_txt = img_prc_txt;
	}
	public String get_img_prc_txt() {
		return img_prc_txt;
	}
	
	//Image Exam Reason
	public void set_img_rsn_txt(String img_rsn_txt) {
		this.img_rsn_txt = img_rsn_txt;
	}
	public String get_img_rsn_txt() {
		return img_rsn_txt;
	}
	
	//Image Description
	public void set_img_dscrp_txt(String img_dscrp_txt) {
		this.img_dscrp_txt = img_dscrp_txt;
	}
	public String get_img_dscrp_txt() {
		return img_dscrp_txt;
	}
	
	//Image Status
	public void set_img_status_txt(String img_status_txt) {
		this.img_status_txt = img_status_txt;
	}
	public String get_img_status_txt() {
		return img_status_txt;
	}
//----------------------------------------------------------------------------------------------------------------------------------------

	public Date getObservationDateTime() {
		return observationDateTime;
	}

	public void setObservationDateTime(Date observationDateTime) {
		this.observationDateTime = observationDateTime;
	}

	public Concept getObservationTypeConcept() {
		return observationTypeConcept;
	}

	public void setObservationTypeConcept(Concept observationTypeConcept) {
		this.observationTypeConcept = observationTypeConcept;
	}

	public Double getValueAsNumber() {
		return valueAsNumber;
	}

	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}

	public String getValueAsString() {
		return valueAsString;
	}

	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}

	public Concept getValueAsConcept() {
		return valueAsConcept;
	}

	public void setValueAsConcept(Concept valueAsConcept) {
		this.valueAsConcept = valueAsConcept;
	}

	public Concept getQualifierConcept () {
		return qualifierConcept;
	}
	
	public void setQualifierConcept (Concept qualifierConcept) {
		this.qualifierConcept = qualifierConcept;
	}
	
	public Concept getUnitConcept() {
		return unitConcept;
	}

	public void setUnitConcept(Concept unitConcept) {
		this.unitConcept = unitConcept;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public VisitOccurrence getVisitOccurrence() {
		return visitOccurrence;
	}

	public void setVisitOccurrence(VisitOccurrence visitOccurrence) {
		this.visitOccurrence = visitOccurrence;
	}

	public String getObservationSourceValue() {
		return observationSourceValue;
	}

	public void setObservationSourceValue(String observationSourceValue) {
		this.observationSourceValue = observationSourceValue;
	}

	public Concept getObservationSourceConcept() {
		return observationSourceConcept;
	}
	
	public void setObservationSourceConcept(Concept observationSourceConcept) {
		this.observationSourceConcept = observationSourceConcept;
	}
	
	public String getUnitSourceValue() {
		return unitSourceValue;
	}

	public void setUnitSourceValue(String unitSourceValue) {
		this.unitSourceValue = unitSourceValue;
	}

	public String getQualifierSourceValue () {
		return qualifierSourceValue;
	}
	
	public void setQualifierSourceValue (String qualifierSourceValue) {
		this.qualifierSourceValue = qualifierSourceValue;
	}
	
	@Override
	public Long getIdAsLong() {
		return getId();
	}

	@Override
	public String getColumnName(String columnVariable) {
		return Observation._getColumnName(columnVariable);
	}
	
	public static String _getColumnName(String columnVariable) {
		try {
			Field field = Observation.class.getDeclaredField(columnVariable);
			if (field != null) {
				Column annotation = field.getDeclaredAnnotation(Column.class);
				if (annotation != null) {
					return Observation._getTableName() + "." + annotation.name();
				} else {
					JoinColumn joinAnnotation = field.getDeclaredAnnotation(JoinColumn.class);
					if (joinAnnotation != null) {
						return Observation._getTableName() + "." + joinAnnotation.name();
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
//			return "observation.observation_id";
//
//		if ("fPerson".equals(columnVariable)) 
//			return "observation.person_id";
//
//		if ("observationConcept".equals(columnVariable)) 
//			return "observation.observation_concept_id";
//
//		if ("date".equals(columnVariable)) 
//			return "observation.observation_date";
//
//		if ("time".equals(columnVariable)) 
//			return "observation.observation_time";
//
//		if ("valueAsString".equals(columnVariable)) 
//			return "observation.value_as_string";
//
//		if ("valueAsNumber".equals(columnVariable)) 
//			return "observation.value_as_number";
//
//		if ("valueAsConcept".equals(columnVariable)) 
//			return "observation.value_as_concept_id";
//
//		if ("typeConcept".equals(columnVariable)) 
//			return "observation.observation_type_concept_id";
//
//		if ("provider".equals(columnVariable)) 
//			return "observation.provider_id";
//
//		if ("visitOccurrence".equals(columnVariable)) 
//			return "observation.visit_occurrence_id";
//
//		if ("sourceValue".equals(columnVariable)) 
//			return "observation.observation_source_value";
//
//		if ("sourceConcept".equals(columnVariable)) 
//			return "observation.observation_source_concept_id";
//
//		if ("qualifierConcept".equals(columnVariable)) 
//			return "observation.qualifier_concept_id";
//
//		if ("qualifierSourceValue".equals(columnVariable)) 
//			return "observation.qualifier_source_value";
//
//		if ("unitConcept".equals(columnVariable)) 
//			return "observation.unit_concept_id";
//
//		if ("unitSourceValue".equals(columnVariable)) 
//			return "observation.unit_source_value";
//
//		return null;
	}

	@Override
	public String getTableName() {
		return Observation._getTableName();
	}
	
	public static String _getTableName() {
		Table annotation = Observation.class.getDeclaredAnnotation(Table.class);
		if (annotation != null) {
			return annotation.name();
		}
		return "observation";
	}

	@Override
	public String getForeignTableName(String foreignVariable) {
		return Observation._getForeignTableName(foreignVariable);
	}
	
	public static String _getForeignTableName(String foreignVariable) {
		if ("observationConcept".equals(foreignVariable) 
				|| "observationTypeConcept".equals(foreignVariable) 
				|| "valueAsConcept".equals(foreignVariable)
				|| "qualifierConcept".equals(foreignVariable) 
				|| "unitConcept".equals(foreignVariable)
				|| "observationSourceConcept".equals(foreignVariable))
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
		return Observation._getSqlTableStatement(parameterList, valueList);
	}

	public static String _getSqlTableStatement(List<String> parameterList, List<String> valueList) {
		return "select * from observation ";
	}

}
