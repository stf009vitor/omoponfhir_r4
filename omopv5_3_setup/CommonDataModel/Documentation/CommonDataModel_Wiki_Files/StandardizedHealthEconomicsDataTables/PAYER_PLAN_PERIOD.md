The PAYER_PLAN_PERIOD table captures details of the period of time that a Person is continuously enrolled under a specific health Plan benefit structure from a given Payer. Each Person receiving healthcare is typically covered by a health benefit plan, which pays for (fully or partially), or directly provides, the care. These benefit plans are provided by payers, such as health insurances or state or government agencies. In each plan the details of the health benefits are defined for the Person or her family, and the health benefit Plan might change over time typically with increasing utilization (reaching certain cost thresholds such as deductibles), plan availability and purchasing choices of the Person. The unique combinations of Payer organizations, health benefit Plans and time periods in which they are valid for a Person are recorded in this table.

Field|Required|Type|Description
:------------------------------|:--------|:------------|:----------------------------------------------
|payer_plan_period_id			|Yes|integer|A identifier for each unique combination of payer, plan, family code and time span.|
|person_id						|Yes|integer|A foreign key identifier to the Person covered by the payer. The demographic details of that Person are stored in the PERSON table.|
|payer_plan_period_start_date	|Yes|date|The start date of the payer plan period.|
|payer_plan_period_end_date		|Yes|date|The end date of the payer plan period.|
|payer_concept_id				|No|integer|A foreign key that refers to a standard Payer concept identifier in the Standarized Vocabularies|
|payer_source_value				|No|varchar(50)|The source code for the payer as it appears in the source data.|
|payer_source_concept_id		|No|integer|A foreign key to a payer concept that refers to the code used in the source.|
|plan_concept_id				|No|integer|A foreign key that refers to a standard plan concept identifier that represents the health benefit plan in the Standardized Vocabularies|
|plan_source_value				|No|varchar(50)|The source code for the Person's health benefit plan as it appears in the source data.|
|plan_source_concept_id			|No|integer|A foreign key to a plan concept that refers to the plan code used in the source data.|
|sponsor_concept_id				|No|integer|A foreign key that refers to a concept identifier that represents the sponsor in the Standardized Vocabularies.|
|sponsor_source_value			|No|varchar(50)|The source code for the Person's sponsor of the health plan as it appears in the source data.|
|sponsor_source_concept_id		|No|integer|A foreign key to a sponsor concept that refers to the sponsor code used in the source data.|
|family_source_value			|No|varchar(50)|The source code for the Person's family as it appears in the source data.|
|stop_reason_concept_id			|No|integer|A foreign key that refers to a standard termination reason that represents the reason for the termination in the Standardized Vocabularies.|
|stop_reason_source_value		|No|varchar(50)|The reason for stop-coverage as it appears in the source data.|
|stop_reason_source_concept_id	|No|integer|A foreign key to a stop-coverage concept that refers to the code used in the source.|

### Conventions 
  * Different Payers have different designs for their health benefit Plans. The PAYER_PLAN_PERIOD table does not capture all details of the plan design or the relationship between Plans or the cost of healthcare triggering a change from one Plan to another. However, it allows identifying the unique combination of Payer (insurer), Plan (determining healthcare benefits and limits) and Person. Typically, depending on healthcare utilization, a Person may have one or many subsequent Plans during coverage by a single Payer.
  * Typically, family members are covered under the same Plan as the Person. In those cases, the payer_source_value, plan_source_value and family_source_value are identical.