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
package edu.gatech.chai.omoponfhir.omopv5.dstu2.provider;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.mapping.OmopMedicationDispense;
import edu.gatech.chai.omoponfhir.omopv5.dstu2.utilities.StaticValues;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import edu.gatech.chai.omopv5.dba.service.ParameterWrapper;

public class MedicationDispenseResourceProvider implements IResourceProvider {

	private WebApplicationContext myAppCtx;
	private OmopMedicationDispense myMapper;
	private int preferredPageSize = 30;

	public MedicationDispenseResourceProvider() {
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		myMapper = new OmopMedicationDispense(myAppCtx);

		String pageSizeStr = myAppCtx.getServletContext().getInitParameter("preferredPageSize");
		if (pageSizeStr != null && pageSizeStr.isEmpty() == false) {
			int pageSize = Integer.parseInt(pageSizeStr);
			if (pageSize > 0) {
				preferredPageSize = pageSize;
			}
		}
	}
	
	public static String getType() {
		return "MedicationDispense";
	}

    public OmopMedicationDispense getMyMapper() {
    	return myMapper;
    }

	private Integer getTotalSize(List<ParameterWrapper> paramList) {
		final Long totalSize;
		if (paramList.size() == 0) {
			totalSize = getMyMapper().getSize();
		} else {
			totalSize = getMyMapper().getSize(paramList);
		}
		
		return totalSize.intValue();
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return MedicationDispense.class;
	}

	/**
	 * The "@Create" annotation indicates that this method implements "create=type", which adds a 
	 * new instance of a resource to the server.
	 */
	@Create()
	public MethodOutcome createMedicationDispense(@ResourceParam MedicationDispense theMedicationDispense) {
		validateResource(theMedicationDispense);
		
		Long id=null;
		try {
			id = myMapper.toDbase(theMedicationDispense, null);
		} catch (FHIRException e) {
			e.printStackTrace();
		}
		
		if (id == null) {
			OperationOutcome outcome = new OperationOutcome();
			CodeableConceptDt detailCode = new CodeableConceptDt();
			detailCode.setText("Failed to create entity.");
			outcome.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails(detailCode);
			throw new UnprocessableEntityException(StaticValues.myFhirContext, outcome);
		}

		return new MethodOutcome(new IdDt(id));
	}

	@Delete()
	public void deleteMedicationDispense(@IdParam IdDt theId) {
		if (myMapper.removeByFhirId(theId) <= 0) {
			throw new ResourceNotFoundException(theId);
		}
	}

	@Update()
	public MethodOutcome updateMedicationDispense(@IdParam IdDt theId, @ResourceParam MedicationDispense theMedicationDispense) {
		validateResource(theMedicationDispense);
		
		Long fhirId=null;
		try {
			fhirId = myMapper.toDbase(theMedicationDispense, theId);
		} catch (FHIRException e) {
			e.printStackTrace();
		}

		if (fhirId == null) {
			throw new ResourceNotFoundException(theId);
		}

		return new MethodOutcome();
	}

	@Read()
	public MedicationDispense readMedicationDispense(@IdParam IdDt theId) {
		MedicationDispense retval = (MedicationDispense) myMapper.toFHIR(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(theId);
		}
			
		return retval;
	}
	
	@Search()
	public IBundleProvider findMedicationRequetsById(
			@RequiredParam(name = MedicationDispense.SP_RES_ID) TokenParam theMedicationDispenseId
			) {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper> ();

		if (theMedicationDispenseId != null) {
			paramList.addAll(myMapper.mapParameter (MedicationDispense.SP_RES_ID, theMedicationDispenseId, false));
		}
				
		MyBundleProvider myBundleProvider = new MyBundleProvider(paramList);
		myBundleProvider.setTotalSize(getTotalSize(paramList));
		myBundleProvider.setPreferredPageSize(preferredPageSize);

		return myBundleProvider;
	}

	@Search()
	public IBundleProvider findMedicationDispensesByParams(
			@OptionalParam(name = MedicationDispense.SP_CODE) TokenOrListParam theOrCodes,
			@OptionalParam(name = MedicationDispense.SP_MEDICATION+"."+Medication.SP_CODE) TokenOrListParam theMedicationOrCodes,
			@OptionalParam(name = MedicationDispense.SP_MEDICATION, chainWhitelist={""}) ReferenceParam theMedication,
			// djogo @OptionalParam(name = MedicationDispense.SP_DATEWRITTEN) DateRangeParam theDateWrittenRange,
//			@OptionalParam(name = MedicationDispense.SP_CONTEXT) ReferenceParam theContext,
//			@OptionalParam(name = MedicationDispense.SP_AUTHOREDON) DateParam theDate,
//			SP Doesn't Exist in DSTU2
			@OptionalParam(name = MedicationDispense.SP_PATIENT) ReferenceParam thePatient
//			@OptionalParam(name = MedicationDispense.SP_SUBJECT) ReferenceParam theSubject
// 			SP Doesn't Exist in DSTU2
			) {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper> ();
		
		if (theOrCodes != null) {
			List<TokenParam> codes = theOrCodes.getValuesAsQueryTokens();
			boolean orValue = true;
			if (codes.size() <= 1)
				orValue = false;
			for (TokenParam code : codes) {
				paramList.addAll(myMapper.mapParameter(MedicationDispense.SP_CODE, code, orValue));
			}
		}

		if (theMedicationOrCodes != null) {
			List<TokenParam> codes = theMedicationOrCodes.getValuesAsQueryTokens();
			boolean orValue = true;
			if (codes.size() <= 1)
				orValue = false;
			for (TokenParam code : codes) {
				paramList.addAll(myMapper.mapParameter("Medication:"+Medication.SP_CODE, code, orValue));
			}
		}
		
		if (theMedication != null) {
			String medicationChain = theMedication.getChain();
			if ("".equals(medicationChain)) {
				paramList.addAll(getMyMapper().mapParameter("Medication:"+Medication.SP_RES_ID, theMedication.getValue(), false));
			}
		}

		// djogo if (theDateWrittenRange != null) {
			// djogo paramList.addAll(myMapper.mapParameter(MedicationDispense.SP_DATEWRITTEN, theDateWrittenRange, false));
		// djogo }

//			SP Doesn't Exist in DSTU2
//		if (theSubject != null) {
//			if (theSubject.getResourceType().equals(PatientResourceProvider.getType())) {
//				thePatient = theSubject;
//			} else {
//				ThrowFHIRExceptions.unprocessableEntityException("We only support Patient resource for subject");
//			}
//		}
		if (thePatient != null) {
			paramList.addAll(myMapper.mapParameter(MedicationDispense.SP_PATIENT, thePatient, false));
		}

		MyBundleProvider myBundleProvider = new MyBundleProvider(paramList);
		myBundleProvider.setTotalSize(getTotalSize(paramList));
		myBundleProvider.setPreferredPageSize(preferredPageSize);
		
		return myBundleProvider;
	}
	
//	private void mapParameter(Map<String, List<ParameterWrapper>> paramMap, String FHIRparam, Object value, boolean or) {
//		List<ParameterWrapper> paramList = myMapper.mapParameter(FHIRparam, value, or);
//		if (paramList != null) {
//			paramMap.put(FHIRparam, paramList);
//		}
//	}

	private void validateResource(MedicationDispense theMedication) {
		// TODO: implement validation method
	}
	
	class MyBundleProvider extends OmopFhirBundleProvider implements IBundleProvider {
		public MyBundleProvider(List<ParameterWrapper> paramList) {
			super(paramList);
			setPreferredPageSize (preferredPageSize);
		}

		@Override
		public List<IBaseResource> getResources(int fromIndex, int toIndex) {
			List<IBaseResource> retv = new ArrayList<IBaseResource>();

			// _Include
			List<String> includes = new ArrayList<String>();

			if (paramList.size() == 0) {
				myMapper.searchWithoutParams(fromIndex, toIndex, retv, includes, null);
			} else {
				myMapper.searchWithParams(fromIndex, toIndex, paramList, retv, includes, null);
			}

			return retv;
		}		
	}
}
