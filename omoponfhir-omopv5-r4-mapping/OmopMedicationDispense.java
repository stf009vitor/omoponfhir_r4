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
 *******************************************************************************/
package edu.gatech.chai.omoponfhir.omopv5.dstu2.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.dstu2.composite.DurationDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.Medication.ProductIngredient;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.api.IDatatype;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.provider.EncounterResourceProvider;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.provider.MedicationDispenseResourceProvider;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.provider.MedicationResourceProvider;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.provider.PatientResourceProvider;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.provider.PractitionerResourceProvider;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.utilities.CodeableConceptUtil;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.utilities.ExtensionUtil;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.utilities.DateUtil;
import edu.gatech.chai.omopv5.dba.service.ConceptService;
import edu.gatech.chai.omopv5.dba.service.DrugExposureService;
import edu.gatech.chai.omopv5.dba.service.FPersonService;
import edu.gatech.chai.omopv5.dba.service.ParameterWrapper;
import edu.gatech.chai.omopv5.dba.service.VisitOccurrenceService;
import edu.gatech.chai.omopv5.model.entity.Concept;
import edu.gatech.chai.omopv5.model.entity.DrugExposure;
import edu.gatech.chai.omopv5.model.entity.FPerson;
import edu.gatech.chai.omopv5.model.entity.Provider;
import edu.gatech.chai.omopv5.model.entity.VisitOccurrence;

import ca.uhn.fhir.model.dstu2.resource.MedicationDispense.DosageInstruction;



/**
 * 
 * @author mc142
 *
 *         concept id OHDSI drug type FHIR 38000179 Physician administered drug
 *         (identified as procedure), MedicationAdministration 38000180
 *         Inpatient administration, MedicationAdministration 43542356 Physician
 *         administered drug (identified from EHR problem list),
 *         MedicationAdministration 43542357 Physician administered drug
 *         (identified from referral record), MedicationAdministration 43542358
 *         Physician administered drug (identified from EHR observation),
 *         MedicationAdministration 581373 Physician administered drug
 *         (identified from EHR order), MedicationAdministration ****** 38000175
 *         Prescription dispensed in pharmacy, MedicationDispense 38000176
 *         Prescription dispensed through mail order, MedicationDispense 581452
 *         Dispensed in Outpatient office, MedicationDispense ****** 38000177
 *         Prescription written, MedicationRequest ****** 44787730 Patient
 *         Self-Reported Medication, MedicationStatement 38000178 Medication
 *         list entry 38000181 Drug era - 0 days persistence window 38000182
 *         Drug era - 30 days persistence window 44777970 Randomized Drug
 */
public class OmopMedicationDispense extends BaseOmopResource<MedicationDispense, DrugExposure, DrugExposureService> {

    private static final Logger logger = LoggerFactory.getLogger(OmopMedicationDispense.class);
    
    /**
     *
     * colocar aqui o codigo drug_type_concept_id referente a medicação dispensada
     *
     **/
	public static Long MEDICATIONREQUEST_CONCEPT_TYPE_ID = 38000177L;
	private static OmopMedicationDispense omopMedicationDispense = new OmopMedicationDispense();
	private VisitOccurrenceService visitOccurrenceService;
	private ConceptService conceptService;
	private FPersonService fPersonService;

	public OmopMedicationDispense(WebApplicationContext context) {
		super(context, DrugExposure.class, DrugExposureService.class, MedicationDispenseResourceProvider.getType());
		initialize(context);
	}

	public OmopMedicationDispense() {
		super(ContextLoaderListener.getCurrentWebApplicationContext(), DrugExposure.class, DrugExposureService.class,
				MedicationDispenseResourceProvider.getType());
		initialize(ContextLoaderListener.getCurrentWebApplicationContext());
	}

	private void initialize(WebApplicationContext context) {
		visitOccurrenceService = context.getBean(VisitOccurrenceService.class);
		conceptService = context.getBean(ConceptService.class);
		fPersonService = context.getBean(FPersonService.class);

        logger.error("Entao, OmopMedicationDispense::initialize");
        
		getSize();
	}

	public static OmopMedicationDispense getInstance() {
		return OmopMedicationDispense.omopMedicationDispense;
	}

	@Override
	public Long toDbase(MedicationDispense fhirResource, IdDt fhirId) throws FHIRException {
		Long omopId = null;
		DrugExposure drugExposure = null;
		if (fhirId != null) {
			omopId = IdMapping.getOMOPfromFHIR(fhirId.getIdPartAsLong(), MedicationDispenseResourceProvider.getType());
		}

		drugExposure = constructOmop(omopId, fhirResource);

		Long retOmopId = null;
		if (omopId == null) {
			retOmopId = getMyOmopService().create(drugExposure).getId();
		} else {
			retOmopId = getMyOmopService().update(drugExposure).getId();
		}

		return IdMapping.getFHIRfromOMOP(retOmopId, MedicationDispenseResourceProvider.getType());
	}

    /**
     * Constrói objeto FHIR a partir de dados OMOP
     * Consulta OMOP a partir do fhirID
     *
     *
     *
     **/
	@Override
	public MedicationDispense constructFHIR(Long fhirId, DrugExposure entity) {

        logger.debug("entao, constructFHIR");
        
        MedicationDispense medicationDispense = new MedicationDispense();

        // seta o ID
		medicationDispense.setId(new IdDt(fhirId));

		// seta o paciente (Subject from FPerson)
		ResourceReferenceDt patientRef = new ResourceReferenceDt(
				new IdDt(PatientResourceProvider.getType(), entity.getFPerson().getId()));
		patientRef.setDisplay(entity.getFPerson().getNameAsSingleString());
		medicationDispense.setPatient(patientRef);

		// Seta a data em que a medicação foi entregue (whenHandedOver)
		Date startDate = entity.getDrugExposureStartDate();
        if ( startDate != null )
			medicationDispense.setWhenHandedOver(new DateTimeDt( startDate ) );

		// See what type of Medication info we want to return
		String medType = System.getenv("MEDICATION_TYPE");
		if (medType != null && !medType.isEmpty() && "local".equalsIgnoreCase(medType)) {
			CodeableConceptDt medicationCodeableConcept;
			Medication medicationResource = new Medication();
			try {
				medicationCodeableConcept = CodeableConceptUtil
						.getCodeableConceptFromOmopConcept(entity.getDrugConcept());
				List<Concept> ingredients = conceptService.getIngredient(entity.getDrugConcept());
				Medication.Product tempProduct = new Medication.Product();
				for (Concept ingredient : ingredients) {
					ProductIngredient medIngredientComponent = new ProductIngredient();
					String temp = ingredient.getId().toString();
					ResourceReferenceDt tempReference = new ResourceReferenceDt("Medication/" + temp);
					medIngredientComponent.setItem(tempReference);
					tempProduct.addIngredient(medIngredientComponent);
//					medicationResource.addIngredient(medIngredientComponent);
				}
				medicationResource.setProduct(tempProduct);
			} catch (FHIRException e) {
				e.printStackTrace();
				return null;
			}
			medicationResource.setCode(medicationCodeableConcept);
			medicationResource.setId("med1");
			List<IResource> tempList = medicationDispense.getContained().getContainedResources();
			tempList.add(medicationResource);
			ContainedDt tempContained = new ContainedDt();
			tempContained.setContainedResources(tempList);
			medicationDispense.setContained(tempContained);
//			medicationDispense.addContained(medicationResource);
			medicationDispense.setMedication(new ResourceReferenceDt("#med1"));
		} else if (medType != null && !medType.isEmpty() && "link".equalsIgnoreCase(medType)) {
			// Get Medication in a reference.
			ResourceReferenceDt medicationReference = new ResourceReferenceDt(
					new IdDt(MedicationResourceProvider.getType(), entity.getDrugConcept().getId()));
			medicationDispense.setMedication(medicationReference);
		} else {
			CodeableConceptDt medicationCodeableConcept;
			try {
				medicationCodeableConcept = CodeableConceptUtil
						.getCodeableConceptFromOmopConcept(entity.getDrugConcept());
			} catch (FHIRException e1) {
				e1.printStackTrace();
				return null;
			}
			medicationDispense.setMedication(medicationCodeableConcept);
		}

		// Dose da droga
		Double dose = entity.getQuantity();
		SimpleQuantityDt doseQuantity = new SimpleQuantityDt();
		if (dose != null) {
			doseQuantity.setValue(dose);
		}

//		Concept unitConcept = entity.getDoseUnitConcept();
		String unitUnit = entity.getDoseUnitSourceValue();
		String unitCode = null;
		String unitSystem = null;
		if (unitUnit != null && !unitUnit.isEmpty()) {
			// See if we can convert this unit to concept code.
			List<Concept> unitConcepts = conceptService.searchByColumnString("concept_name", unitUnit);
			if (unitConcepts.size() > 0) {
				String omopUnitVocab = unitConcepts.get(0).getVocabularyId();
				String omopUnitCode = unitConcepts.get(0).getConceptCode();
				String omopUnitName = unitConcepts.get(0).getConceptName();
				String fhirUnitUri;
				try {
					fhirUnitUri = OmopCodeableConceptMapping.fhirUriforOmopVocabulary(omopUnitVocab);
					if ("None".equals(fhirUnitUri)) {
//						fhirUnitUri = unitConcept.getVocabulary().getVocabularyReference();
						fhirUnitUri = "NotAvailable";
					}

					unitUnit = omopUnitName;
					unitCode = omopUnitCode;
					unitSystem = fhirUnitUri;
				} catch (FHIRException e) {
					e.printStackTrace();
				}
			}
		}

        
		if (!doseQuantity.isEmpty()) {
			if (unitUnit != null && !unitUnit.isEmpty()) doseQuantity.setUnit(unitUnit);
			if (unitCode != null && !unitCode.isEmpty()) doseQuantity.setCode(unitCode);
			if (unitSystem != null && !unitSystem.isEmpty()) doseQuantity.setSystem(unitSystem);

			DosageInstruction dosage = new DosageInstruction();
			dosage.setDose(doseQuantity);
			medicationDispense.addDosageInstruction(dosage);
		}
        

		// dispense request mapping.
        /* djogo
		Integer refills = entity.getRefills();
		DispenseRequest dispenseRequest = new DispenseRequest();
		if (refills != null) {
			dispenseRequest.setNumberOfRepeatsAllowed(refills);
		}

		Double quantity = entity.getQuantity();
		if (quantity != null) {
			SimpleQuantityDt simpleQty = new SimpleQuantityDt();
			simpleQty.setValue(quantity);
			simpleQty.setUnit(unitUnit);
			simpleQty.setCode(unitCode);
			simpleQty.setSystem(unitSystem);
			dispenseRequest.setQuantity(simpleQty);
		}

		Integer daysSupply = entity.getDaysSupply();
		if (daysSupply != null) {
			DurationDt qty = new DurationDt();
			qty.setValue(daysSupply);
			// Set the UCUM unit to day.
			String fhirUri = OmopCodeableConceptMapping.UCUM.getFhirUri();
			qty.setSystem(fhirUri);
			qty.setCode("d");
			qty.setUnit("day");
			dispenseRequest.setExpectedSupplyDuration(qty);
		}

		if (!dispenseRequest.isEmpty()) {
			medicationDispense.setDispenseRequest(dispenseRequest);
		}
        */

		// Recorder mapping

        Provider provider = entity.getProvider();
		if (provider != null) {
			ResourceReferenceDt recorderReference = new ResourceReferenceDt(
					new IdDt(PractitionerResourceProvider.getType(), provider.getId()));
//			recorderReference.setDisplay(provider.getProviderName());
			medicationDispense.setDispenser(recorderReference); /// djogo
		}
        

		// Context mapping
		/* djogo
        VisitOccurrence visitOccurrence = entity.getVisitOccurrence();
		if (visitOccurrence != null) {
			ResourceReferenceDt contextReference = new ResourceReferenceDt(
					new IdDt(EncounterResourceProvider.getType(), visitOccurrence.getId()));
			medicationDispense.setEncounter(contextReference);
		}
        //*/

		return medicationDispense;
	}

	@Override
	public List<ParameterWrapper> mapParameter(String parameter, Object value, boolean or) {
        logger.debug("entao, mapParameter");

		List<ParameterWrapper> mapList = new ArrayList<ParameterWrapper>();
		ParameterWrapper paramWrapper = new ParameterWrapper();
		if (or)
			paramWrapper.setUpperRelationship("or");
		else
			paramWrapper.setUpperRelationship("and");

		switch (parameter) {
		case MedicationDispense.SP_RES_ID:
			String medicationDispenseId = ((TokenParam) value).getValue();
			paramWrapper.setParameterType("Long");
			paramWrapper.setParameters(Arrays.asList("id"));
			paramWrapper.setOperators(Arrays.asList("="));
			paramWrapper.setValues(Arrays.asList(medicationDispenseId));
			paramWrapper.setRelationship("or");
			mapList.add(paramWrapper);
			break;
		case MedicationDispense.SP_CODE:
		case "Medication:" + Medication.SP_CODE:
			String system = ((TokenParam) value).getSystem();
			String code = ((TokenParam) value).getValue();

			if ((system == null || system.isEmpty()) && (code == null || code.isEmpty()))
				break;

			String omopVocabulary = "None";
			if (system != null && !system.isEmpty()) {
				try {
//					omopVocabulary = OmopCodeableConceptMapping.omopVocabularyforFhirUri(system);
					omopVocabulary = fhirOmopVocabularyMap.getOmopVocabularyFromFhirSystemName(system);
				} catch (FHIRException e) {
					e.printStackTrace();
					break;
				}
			}

			paramWrapper.setParameterType("String");
			if ("None".equals(omopVocabulary) && code != null && !code.isEmpty()) {
				paramWrapper.setParameters(Arrays.asList("drugConcept.conceptCode"));
				paramWrapper.setOperators(Arrays.asList("like"));
				paramWrapper.setValues(Arrays.asList(code));
				paramWrapper.setRelationship("or");
			} else if (!"None".equals(omopVocabulary) && (code == null || code.isEmpty())) {
				paramWrapper.setParameters(Arrays.asList("drugConcept.vocabularyId"));
				paramWrapper.setOperators(Arrays.asList("like"));
				paramWrapper.setValues(Arrays.asList(omopVocabulary));
				paramWrapper.setRelationship("or");
			} else {
				paramWrapper.setParameters(Arrays.asList("drugConcept.vocabularyId", "drugConcept.conceptCode"));
				paramWrapper.setOperators(Arrays.asList("like", "like"));
				paramWrapper.setValues(Arrays.asList(omopVocabulary, code));
				paramWrapper.setRelationship("and");
			}
			mapList.add(paramWrapper);
			break;
                /*
		case MedicationDispense.SP_ENCOUNTER:
			Long fhirEncounterId = ((ReferenceParam) value).getIdPartAsLong();
			Long omopVisitOccurrenceId = IdMapping.getOMOPfromFHIR(fhirEncounterId,
					EncounterResourceProvider.getType());
//			String resourceName = ((ReferenceParam) value).getResourceType();

			// We support Encounter so the resource type should be Encounter.
			if (omopVisitOccurrenceId != null) {
				paramWrapper.setParameterType("Long");
				paramWrapper.setParameters(Arrays.asList("visitOccurrence.id"));
				paramWrapper.setOperators(Arrays.asList("="));
				paramWrapper.setValues(Arrays.asList(String.valueOf(omopVisitOccurrenceId)));
				paramWrapper.setRelationship("or");
				mapList.add(paramWrapper);
			}
			break;
		case MedicationDispense.SP_DATEWRITTEN:
			DateRangeParam dateRangeParam = ((DateRangeParam) value);
			DateUtil.constructParameterWrapper(dateRangeParam, "drugExposureStartDate", paramWrapper, mapList);
			ParameterWrapper paramWrapper1 = new ParameterWrapper();
			paramWrapper1.setUpperRelationship("or");
			DateUtil.constructParameterWrapper(dateRangeParam, "drugExposureEndDate", paramWrapper1, mapList);
			break;
            */
//		case MedicationDispense.SP_AUTHOREDON:
//			DateParam authoredOnDataParam = ((DateParam) value);
//			ParamPrefixEnum apiOperator = authoredOnDataParam.getPrefix();
//			String sqlOperator = null;
//			if (apiOperator.equals(ParamPrefixEnum.GREATERTHAN)) {
//				sqlOperator = ">";
//			} else if (apiOperator.equals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS)) {
//				sqlOperator = ">=";
//			} else if (apiOperator.equals(ParamPrefixEnum.LESSTHAN)) {
//				sqlOperator = "<";
//			} else if (apiOperator.equals(ParamPrefixEnum.LESSTHAN_OR_EQUALS)) {
//				sqlOperator = "<=";
//			} else if (apiOperator.equals(ParamPrefixEnum.NOT_EQUAL)) {
//				sqlOperator = "!=";
//			} else {
//				sqlOperator = "=";
//			}
//			Date authoredOnDate = authoredOnDataParam.getValue();
//
//			paramWrapper.setParameterType("Date");
//			paramWrapper.setParameters(Arrays.asList("drugExposureStartDate"));
//			paramWrapper.setOperators(Arrays.asList(sqlOperator));
//			paramWrapper.setValues(Arrays.asList(String.valueOf(authoredOnDate.getTime())));
//			paramWrapper.setRelationship("or");
//			mapList.add(paramWrapper);
//			break;
		case MedicationDispense.SP_PATIENT:
//		case MedicationDispense.SP_SUBJECT:
			ReferenceParam patientReference = ((ReferenceParam) value);
			Long fhirPatientId = patientReference.getIdPartAsLong();
			Long omopPersonId = IdMapping.getOMOPfromFHIR(fhirPatientId, PatientResourceProvider.getType());

			String omopPersonIdString = String.valueOf(omopPersonId);

			paramWrapper.setParameterType("Long");
			paramWrapper.setParameters(Arrays.asList("fPerson.id"));
			paramWrapper.setOperators(Arrays.asList("="));
			paramWrapper.setValues(Arrays.asList(omopPersonIdString));
			paramWrapper.setRelationship("or");
			mapList.add(paramWrapper);
			break;
		case "Medication:" + Medication.SP_RES_ID:
			String pId = (String) value;
			paramWrapper.setParameterType("Long");
			paramWrapper.setParameters(Arrays.asList("drugConcept.id"));
			paramWrapper.setOperators(Arrays.asList("="));
			paramWrapper.setValues(Arrays.asList(pId));
			paramWrapper.setRelationship("or");
			mapList.add(paramWrapper);
			break;
		default:
			mapList = null;
		}

		return mapList;
	}

	final ParameterWrapper filterParam = new ParameterWrapper("Long", Arrays.asList("drugTypeConcept.id"),
			Arrays.asList("="), Arrays.asList(String.valueOf(OmopMedicationDispense.MEDICATIONREQUEST_CONCEPT_TYPE_ID)),
			"or");

	@Override
	public Long getSize() {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper>();
		// call getSize with empty parameter list. The getSize will add filter
		// parameter.
        logger.debug("entao, getSize()");
		Long size = getSize(paramList);

		ExtensionUtil.addResourceCount(getMyFhirResourceType(), size);

		return size;
	}

	@Override
	public Long getSize(List<ParameterWrapper> paramList) {
        logger.debug("entao, getSize(paramList)");
        
        //for ( ParameterWrapper p : paramList ) {
          //  logger.debug( p );
        //}
        
		paramList.add(filterParam);

		return getMyOmopService().getSize(paramList);
	}

	@Override
	public void searchWithoutParams(int fromIndex, int toIndex, List<IBaseResource> listResources,
			List<String> includes, String sort) {

        logger.debug("entao, searchWithoutParams");
        // This is read all. But, since we will add an exception conditions to add
		// filter.
		// we will call the search with params method.
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper>();
		searchWithParams(fromIndex, toIndex, paramList, listResources, includes, sort);
	}

	@Override
	public void searchWithParams(int fromIndex, int toIndex, List<ParameterWrapper> mapList,
			List<IBaseResource> listResources, List<String> includes, String sort) {
		mapList.add(filterParam);

        logger.debug("entao, searchWithParams");
		List<DrugExposure> entities = getMyOmopService().searchWithParams(fromIndex, toIndex, mapList, sort);

		for (DrugExposure entity : entities) {
			Long omopId = entity.getIdAsLong();
			Long fhirId = IdMapping.getFHIRfromOMOP(omopId, getMyFhirResourceType());
			MedicationDispense fhirResource = constructResource(fhirId, entity, includes);
			if (fhirResource != null) {
				listResources.add(fhirResource);
				// Do the rev_include and add the resource to the list.
				addRevIncludes(omopId, includes, listResources);
			}

		}
	}

	@Override
	public DrugExposure constructOmop(Long omopId, MedicationDispense fhirResource) {
        logger.debug("entao, constructOmop");
		DrugExposure drugExposure = null;
		if (omopId != null) {
			// Update
			drugExposure = getMyOmopService().findById(omopId);
			if (drugExposure == null) {
				try {
					throw new FHIRException(fhirResource.getId() + " does not exist");
				} catch (FHIRException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Create
            /*
			List<IdentifierDt> identifiers = fhirResource.getIdentifier();
			for (IdentifierDt identifier : identifiers) {
				if (identifier.isEmpty())
					continue;
				String identifierValue = identifier.getValue();
				List<DrugExposure> results = getMyOmopService().searchByColumnString("drugSourceValue",
						identifierValue);
				if (results.size() > 0) {
					drugExposure = results.get(0);
					omopId = drugExposure.getId();
					break;
				}
			}
            */

			/*
            if (drugExposure == null) {
				drugExposure = new DrugExposure();
				// Add the source column.
				IdentifierDt identifier = fhirResource.getIdentifierFirstRep();
				if (!identifier.isEmpty()) {
					drugExposure.setDrugSourceValue(identifier.getValue());
				}
			}
            */
		}

		// Set patient.
		ResourceReferenceDt patientReference = fhirResource.getPatient();
		if (patientReference == null)
			try {
				throw new FHIRException("Patient must exist.");
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		Long patientFhirId = patientReference.getReferenceElement().getIdPartAsLong();
		Long omopFPersonId = IdMapping.getOMOPfromFHIR(patientFhirId, PatientResourceProvider.getType());

		FPerson fPerson = fPersonService.findById(omopFPersonId);
		if (fPerson == null)
			try {
				throw new FHIRException("Patient/" + patientFhirId + " is not valid");
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		drugExposure.setFPerson(fPerson);

		// Get medication[x]
		IDatatype medicationType = fhirResource.getMedication();
		Concept omopConcept = null;
		CodeableConceptDt medicationCodeableConcept = null;
		if (medicationType instanceof ResourceReferenceDt) {
			// We may have reference.
			try {
//				medicationReference = fhirResource.getMedicationReference();
//				medicationReference = fhirResource.getMedication();
				if (medicationType.isEmpty()) {
					// This is an error. We require this.
					throw new FHIRException("Medication[CodeableConcept or Reference] is missing");
				} else {
					String medicationReferenceId = ((ResourceReferenceDt) medicationType).getReferenceElement()
							.getIdPart();
					if (((ResourceReferenceDt) medicationType).getReferenceElement().isLocal()) {
//						List<ResourceReferenceDt> contains = fhirResource.getContained();
						List<IResource> contains = fhirResource.getContained().getContainedResources();
						for (IResource resource : contains) {
							if (!resource.isEmpty()
									&& resource.getIdElement().getIdPart().equals(medicationReferenceId)) {

								// This must medication resource.
								Medication medicationResource = (Medication) resource;
								medicationCodeableConcept = medicationResource.getCode();
								break;
							}
						}
					} else {
						throw new FHIRException("Medication Reference must have the medication in the contained");
					}
				}
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
//				medicationCodeableConcept = fhirResource.getMedicationCodeableConcept();
				if (!(fhirResource.getMedication() instanceof CodeableConceptDt))
					throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "
							+ fhirResource.getMedication().getClass().getName() + " was encountered");
				medicationCodeableConcept = (CodeableConceptDt) fhirResource.getMedication();
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (medicationCodeableConcept == null || medicationCodeableConcept.isEmpty()) {
			try {
				throw new FHIRException("Medication[CodeableConcept or Reference] could not be mapped");
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			omopConcept = CodeableConceptUtil.searchConcept(conceptService, medicationCodeableConcept);
			if (omopConcept == null) {
				throw new FHIRException("Medication[CodeableConcept or Reference] could not be found");
			} else {
				drugExposure.setDrugConcept(omopConcept);
			}
		} catch (FHIRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set drug exposure type
		Concept drugExposureType = new Concept();
		drugExposureType.setId(MEDICATIONREQUEST_CONCEPT_TYPE_ID);
		drugExposure.setDrugTypeConcept(drugExposureType);

		// Set start date from authored on date
		Date whenPrepared = fhirResource.getWhenPrepared();
		// djogo Date authoredDate = fhirResource.getDateWritten();
		drugExposure.setDrugExposureStartDate(whenPrepared);

		// Set VisitOccurrence
        /*
		ResourceReferenceDt encounterReference = fhirResource.getEncounter();
		if (encounterReference != null && !encounterReference.isEmpty()) {
			if (EncounterResourceProvider.getType()
					.equals(encounterReference.getReferenceElement().getResourceType())) {
				// Get fhirIDLong.
				Long fhirEncounterIdLong = encounterReference.getReferenceElement().getIdPartAsLong();
				Long omopEncounterId = IdMapping.getOMOPfromFHIR(fhirEncounterIdLong,
						EncounterResourceProvider.getType());
				if (omopEncounterId != null) {
					VisitOccurrence visitOccurrence = visitOccurrenceService.findById(omopEncounterId);
					if (visitOccurrence != null)
						drugExposure.setVisitOccurrence(visitOccurrence);
				} else {
					try {
						throw new FHIRException("Encounter/" + fhirEncounterIdLong + " is not valid.");
					} catch (FHIRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
        */

		// dosageInstruction
		List<DosageInstruction> dosageInstructions = fhirResource.getDosageInstruction();
		for (DosageInstruction dosageInstruction : dosageInstructions) {
			SimpleQuantityDt doseQty;
			try {
				doseQty = (SimpleQuantityDt) dosageInstruction.getDose();
				if (doseQty.isEmpty())
					continue;
				drugExposure.setQuantity(doseQty.getValue().doubleValue());
				String doseCode = doseQty.getCode();
				String doseSystem = doseQty.getSystem();
				String vocabId = OmopCodeableConceptMapping.omopVocabularyforFhirUri(doseSystem);
				Concept unitConcept = CodeableConceptUtil.getOmopConceptWithOmopVacabIdAndCode(conceptService, vocabId,
						doseCode);
				if (unitConcept != null && unitConcept.getId() != 0L)
					drugExposure.setDoseUnitSourceValue(unitConcept.getConceptName());
				else 
					drugExposure.setDoseUnitSourceValue(doseQty.getCode());
				break;
			} catch (FHIRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
        
        /*
		// dispense request
		DispenseRequest dispenseRequest = fhirResource.getDispenseRequest();
		if (dispenseRequest != null && !dispenseRequest.isEmpty()) {
			Integer refills = dispenseRequest.getNumberOfRepeatsAllowed();
			if (refills != null) {
				drugExposure.setRefills(refills);
			}

			SimpleQuantityDt qty = dispenseRequest.getQuantity();
			if (qty != null) {
				drugExposure.setQuantity(qty.getValue().doubleValue());
				String doseCode = qty.getCode();
				String doseSystem = qty.getSystem();
				String vocabId;
				try {
					vocabId = OmopCodeableConceptMapping.omopVocabularyforFhirUri(doseSystem);
					Concept unitConcept = CodeableConceptUtil.getOmopConceptWithOmopVacabIdAndCode(conceptService,
							vocabId, doseCode);
					if (unitConcept != null && unitConcept.getId() != 0L)
						drugExposure.setDoseUnitSourceValue(unitConcept.getConceptName());
					else
						drugExposure.setDoseUnitSourceValue(doseCode);
				} catch (FHIRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        */

//		ResourceReferenceDt practitionerRef = fhirResource.getRecorder();
//		if (practitionerRef != null && !practitionerRef.isEmpty()) {
//			Long fhirPractitionerIdLong =
//					practitionerRef.getReferenceElement().getIdPartAsLong();
//			Long omopProviderId = IdMapping.getOMOPfromFHIR(fhirPractitionerIdLong, PractitionerResourceProvider.getType());
//			Provider provider = providerService.findById(omopProviderId);
//			if (provider != null) {
//				drugExposure.setProvider(provider);
//			}
//		}

		return drugExposure;
	}
}
