package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisResult {

    public enum PatientType {

        VIOLATED("Patients that violated by taking B and I together."), //
        VALID_NO_COMED("Patients that did not violate, because they never took B and I together."), //
        VALID_BI_SWITCH("Patients that did not violate, because they switched from B to I."), //
        VALID_IB_SWITCH("Patients that did not violate, because they switched from I to B."), //
        VALID_I_TRIAL("Patients that did not violate, because they simply trialled I during B."), //
        VALID_B_TRIAL("Patients that did not violate, because they simply trialled B during I.");

        public String name;

        PatientType(String name) {
            this.name = name;
        }
    }

    public Map<PatientType, Integer> patients = new HashMap<PatientType, Integer>();
    public int numRecords;

    public static AnalysisResult performBIAnalysis() throws IOException {
        List<PurchaseRecord> purchaseRecords = PurchaseRecord.loadFromFile();
        List<List<PurchaseRecord>> purchaseRecordsByDay = 
        		RecordHandler.getRecordsByDay(purchaseRecords); //TODO : Maybe remove this later
        List<List<PurchaseRecord>> purchaseRecordsByPatientID = 
        		RecordHandler.getRecordsByPatientID(purchaseRecordsByDay);
        
        
        //Instantiate the map
        AnalysisResult result = new AnalysisResult();
        result.numRecords = purchaseRecords.size();
        result.patients.put(PatientType.VIOLATED, 0);
        result.patients.put(PatientType.VALID_NO_COMED, 0);
        result.patients.put(PatientType.VALID_BI_SWITCH, 0);
        result.patients.put(PatientType.VALID_IB_SWITCH, 0);
        result.patients.put(PatientType.VALID_I_TRIAL, 0);
        result.patients.put(PatientType.VALID_B_TRIAL, 0);
        
        for(List<PurchaseRecord> patientRecords : purchaseRecordsByPatientID) {
        	if(patientRecords.size() == 0) continue;
        	AnalysisResult.PatientType classification = PatientClassification.getClassification(patientRecords);
        	result.patients.put(classification, result.patients.get(classification) + 1);
        }
        
        
        // do some processing here

        // then put real results in here
        /*
        AnalysisResult result = new AnalysisResult();
        result.patients.put(PatientType.VIOLATED, 200);
        result.patients.put(PatientType.VALID_NO_COMED, 500);
        result.patients.put(PatientType.VALID_BI_SWITCH, 100);
        result.patients.put(PatientType.VALID_IB_SWITCH, 100);
        result.patients.put(PatientType.VALID_I_TRIAL, 50);
        result.patients.put(PatientType.VALID_B_TRIAL, 50);*/
        return result;
    }
}