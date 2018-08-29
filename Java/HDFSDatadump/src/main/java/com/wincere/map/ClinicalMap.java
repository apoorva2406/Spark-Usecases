package com.wincere.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import com.wincere.analytics.xobject.audit.AuditRecord;
import com.wincere.analytics.xobject.audit.ClinicalData;
import com.wincere.analytics.xobject.audit.Comment;
import com.wincere.analytics.xobject.audit.FormData;
import com.wincere.analytics.xobject.audit.ItemData;
import com.wincere.analytics.xobject.audit.ItemGroupData;
import com.wincere.analytics.xobject.audit.LocationRef;
import com.wincere.analytics.xobject.audit.ProtocolDeviation;
import com.wincere.analytics.xobject.audit.Query;
import com.wincere.analytics.xobject.audit.Review;
import com.wincere.analytics.xobject.audit.Signature;
import com.wincere.analytics.xobject.audit.SiteRef;
import com.wincere.analytics.xobject.audit.StudyEventData;
import com.wincere.analytics.xobject.audit.SubjectData;
import com.wincere.analytics.xobject.audit.UserRef;

public class ClinicalMap implements Serializable {

	public JavaRDD<Map<String, String>> run(JavaRDD<ClinicalData> clinical) {
		
	
		JavaRDD<Map<String, String>> clinicalMap= clinical.map(new Function<ClinicalData ,Map<String, String>>(){
			public Map<String, String> call(ClinicalData clinical){
				Map<String, String> m = new HashMap<String, String>();
				if (clinical != null) {

					m.put("Level", "Subject");

					// Information from ClinicalData object
					SubjectData subjectData = clinical.getSubjectData();
					String AuditSubCategoryName = clinical.getAuditSubCategoryName();
					m.put("AuditSubCategoryName", AuditSubCategoryName);
					String studyOID = clinical.getStudyOID();
					m.put("studyOID", studyOID);
					String metaDataVersionOID = clinical.getMetaDataVersionOID();
					m.put("metaDataVersionOID", metaDataVersionOID);

					// Information from SubjectData object
					SiteRef siteRef = subjectData.getSiteRef();
					String siteLocationOID = siteRef.getLocationOID();
					m.put("siteLocationOID", siteLocationOID);
					StudyEventData studies = subjectData.getStudies();
					String subjectKey = subjectData.getSubjectKey();
					m.put("subjectKey", subjectKey);
					String subjectKeyType = subjectData.getSubjectKeyType();
					m.put("subjectKeyType", subjectKeyType);
					String subjectName = subjectData.getSubjectName();
					m.put("subjectName", subjectName);
					String subjectStatus = subjectData.getSubjectStatus();
					m.put("subjectStatus", subjectStatus);

					AuditRecord auditRecord;
					auditRecord = subjectData.getAuditRecord();

					Signature signature = null;

					// Information from StudyEventData object
					if (studies != null) {

						m.put("Level", "StudyEvent");
						if (auditRecord == null)
							auditRecord = studies.getAuditRecords();

						FormData formData = studies.getFormData();
						String studyEventOID = studies.getStudyEventOID();
						m.put("studyEventOID", studyEventOID);
						String studyEventRepeatKey = studies.getStudyEventRepeatKey();
						m.put("studyEventRepeatKey", studyEventRepeatKey);

						// Information from FormData object
						if (formData != null) {

							m.put("Level", "Form");
							if (auditRecord == null)
								auditRecord = formData.getAuditRecords();

							signature = formData.getSignature();

							ItemGroupData itemGroupData = formData.getItemGroupData();
							String formOID = formData.getFormOID();
							m.put("formOID", formOID);
							String formRepeatKey = formData.getFormRepeatKey();
							m.put("formRepeatKey", formRepeatKey);

							// Information from ItemGroupData object
							if (itemGroupData != null) {

								m.put("Level", "ItemGroup");
								if (auditRecord == null)
									auditRecord = itemGroupData.getAuditRecords();

								ItemData itemData = itemGroupData.getItemData();
								String itemGroupOID = itemGroupData.getItemGroupOID();
								m.put("itemGroupOID", itemGroupOID);
								String itemGroupRepeatKey = itemGroupData
										.getItemGroupRepeatKey();
								m.put("itemGroupRepeatKey", itemGroupRepeatKey);
								String itemGroupTransactionType = itemGroupData
										.getTransactionType();
								m.put("itemGroupTransactionType",
										itemGroupTransactionType);

								// Information from ItemData object
								if (itemData != null) {
									
									m.put("Level", "Item");
									
									String itemOID = itemData.getItemOID();
									m.put("itemOID", itemOID);
									
									m.put("datapointID", studyOID +"|"+  siteLocationOID+"|"+subjectKey+"|"+studyEventOID+"|"+studyEventRepeatKey+ "|"+formOID+"|"+formRepeatKey+"|"+itemGroupOID+"|"+itemGroupRepeatKey+"|"+itemOID);
									if (auditRecord == null)
										auditRecord = itemData.getAuditRecords();

									if (signature == null)
										signature = itemData.getSignature();

									Query query = itemData.getQuery();
									Comment comment = itemData.getComment();
									Review review = itemData.getReview();

									String measurementUnitRef = itemData
											.getMeasurementUnitRef();
									m.put("measurementUnitRef", measurementUnitRef);
									ProtocolDeviation protocolDeviation = itemData
											.getProtocolDeviation();
									
									String itemTransactionType = itemData
											.getTransactionType();
									m.put("itemTransactionType", itemTransactionType);
									String itemValue = itemData.getValue();
									m.put("itemValue", itemValue);
									String verify = itemData.getVerify();
									m.put("itemVerify", verify);
									String lock = itemData.getLock();
									m.put("itemLock", lock);
									String freeze = itemData.getFreeze();
									m.put("itemFreeze", freeze);
									

									// Information from Query object
									if (query != null) {

										String queryRepeatKey = query
												.getQueryRepeatKey();
										m.put("queryRepeatKey", queryRepeatKey);
										String recipient = query.getRecipient();
										m.put("queryRecipient", recipient);
										String status = query.getStatus();
										m.put("queryStatus", status);
										String queryValue = query.getValue();
										m.put("queryValue", queryValue);
									}

									// Information from Comment object
									if (comment != null) {

										String commentRepeatKey = comment
												.getCommentRepeatKey();
										m.put("commentRepeatKey", commentRepeatKey);
										String commentTransactionType = comment
												.getTransactionType();
										m.put("commentTransactionType",
												commentTransactionType);
										String commentValue = comment.getValue();
										m.put("commentValue", commentValue);
									}

									// Information from Review object
									if (review != null) {

										String groupName = review.getGroupName();
										m.put("groupName", groupName);
										String reviewed = review.getReviewed();
										m.put("reviewed", reviewed);
									}

									// Information from ProtocolDeviation object
									if (protocolDeviation != null) {
										String pdTransactionType = protocolDeviation
												.getTransactionType();
										m.put("pdTransactionType", pdTransactionType);
										String pdValue = protocolDeviation.getValue();
										m.put("pdValue", pdValue);
										String pdStatus = protocolDeviation.getStatus();
										m.put("pdStatus", pdStatus);
										String pdCode = protocolDeviation.getCode();
										m.put("pdCode", pdCode);
										String pdclassName = protocolDeviation
												.getClassName();
										m.put("pdclassName", pdclassName);
										String protocolDeviationRepeatKey = protocolDeviation
												.getProtocolDeviationRepeatKey();
										m.put("protocolDeviationRepeatKey",
												protocolDeviationRepeatKey);
									}

								}
							}
						}
					}

					// Information from Audit Record object
					UserRef userRef = auditRecord.getUserRef();
					LocationRef auditLocationRef = auditRecord.getLocationRef();
					String auditDateTimeStamp = auditRecord.getDateTimeStamp();
					m.put("auditDateTimeStamp", auditDateTimeStamp);
					String auditReasonForChange = auditRecord.getReasonForChange();
					m.put("auditReasonForChange", auditReasonForChange);
					String sourceID = auditRecord.getSourceID();
					m.put("sourceID", sourceID);
					String auditUserOID = userRef.getUserOID();
					m.put("auditUserOID", auditUserOID);
					String auditUserLocationOID = auditLocationRef.getLocationOID();
					m.put("auditUserLocationOID", auditUserLocationOID);

					// Information from Signature object
					if (signature != null) {
						String signatureUserRef = signature.getUserRef();
						m.put("signatureUserRef", signatureUserRef);
						String signatureLocationRef = signature.getLocationRef();
						m.put("signatureLocationRef", signatureLocationRef);
						String signatureRef = signature.getSignatureRef();
						m.put("signatureRef", signatureRef);
						String signatureDateTimeStamp = signature.getDateTimeStamp();
						m.put("signatureDateTimeStamp", signatureDateTimeStamp);
					}

				}
				return m;
			}
		});
		
		return clinicalMap;
		
	}

}
